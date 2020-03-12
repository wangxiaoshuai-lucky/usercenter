package com.kelab.usercenter.serivce.impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.builder.UserInfoBuilder;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.SettingsConstant;
import com.kelab.usercenter.constant.StatusMsgConstant;
import com.kelab.usercenter.constant.UserInfoConstant;
import com.kelab.usercenter.constant.enums.CacheConstant;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.convert.UserInfoConvert;
import com.kelab.usercenter.dal.domain.SiteSettingDomain;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.SiteSettingRepo;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.usercenter.dal.repo.UserRankRepo;
import com.kelab.usercenter.resultVO.SingleResult;
import com.kelab.usercenter.sender.MailSender;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.md5.Md5Util;
import com.kelab.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserInfoService {

    private UserInfoRepo userInfoRepo;

    private UserRankRepo userRankRepo;

    private SiteSettingRepo siteSettingRepo;

    private RedisCache redisCache;

    private UserLoginLogRepo userLoginLogRepo;

    private OnlineService onlineService;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoRepo userInfoRepo
            , UserRankRepo userRankRepo
            , SiteSettingRepo siteSettingRepo
            , RedisCache redisCache
            , UserLoginLogRepo userLoginLogRepo
            , OnlineService onlineService) {
        this.userInfoRepo = userInfoRepo;
        this.userRankRepo = userRankRepo;
        this.siteSettingRepo = siteSettingRepo;
        this.redisCache = redisCache;
        this.userLoginLogRepo = userLoginLogRepo;
        this.onlineService = onlineService;
    }

    @Override
    public LoginResult login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = new LoginResult();
        if (!verifyCode.equalsIgnoreCase(redisCache.get(CacheConstant.VERIFY_CODE, uuid))) {
            result.setStatus(StatusMsgConstant.VERIFY_CODE_ERROR);
            return result;
        }
        // 校验验证码后验证码失效
        redisCache.delete(CacheConstant.VERIFY_CODE, uuid);
        UserInfoDomain domain = userInfoRepo.queryByUsername(username, false);
        if (domain == null) {
            result.setStatus(StatusMsgConstant.USER_NOT_EXIST_ERROR);
        } else if (!Md5Util.StringInMd5(password).equals(domain.getPassword())) {
            result.setStatus(StatusMsgConstant.PWD_ERROR);
        } else {
            result = loginSuccess(domain);
        }
        return result;
    }

    @Override
    public LoginResult register(Context context, UserInfo userInfo) {
        LoginResult result = new LoginResult();
        UserInfoDomain userInfoDomain = UserInfoBuilder.buildNewUser(userInfo);
        // 密码 md5 加密一次
        userInfoDomain.setPassword(Md5Util.StringInMd5(userInfoDomain.getPassword()));
        // username 查重
        if (userInfoRepo.queryByUsername(userInfoDomain.getUsername(), false) != null) {
            result.setStatus(StatusMsgConstant.USER_EXISTED_ERROR);
            return result;
        }
        // stuId 查重
        if (userInfoRepo.queryByStudentId(userInfoDomain.getStudentId(), false) != null) {
            result.setStatus(StatusMsgConstant.STUDENT_ID_EXISTED_ERROR);
            return result;
        }
        userInfoRepo.save(userInfoDomain);
        return loginSuccess(userInfoDomain);
    }

    @Override
    public String resetPwd(Context context, String username, String verifyCode, String uuid) {
        String originCode = redisCache.get(CacheConstant.VERIFY_CODE, uuid);
        if (!verifyCode.equalsIgnoreCase(originCode)) {
            return StatusMsgConstant.VERIFY_CODE_ERROR;
        }
        redisCache.delete(CacheConstant.VERIFY_CODE, uuid);
        UserInfoDomain userInfoDomain = userInfoRepo.queryByUsername(username, false);
        if (userInfoDomain == null) {
            return StatusMsgConstant.USER_NOT_EXIST_ERROR;
        }
        // 获取模板内容
        Map<Integer, String> settings = getSiteSetting();
        String expire = settings.get(SettingsConstant.RESET_PWD_MAIL_EXP);
        String urlTemplate = settings.get(SettingsConstant.RESET_PWD_FRONT_END_URL);
        String subject = settings.get(SettingsConstant.RESET_PWD_SUBJECT);
        String content = settings.get(SettingsConstant.RESET_PWD_CONTENT);
        // 生成token
        String token = tokenForResetPwd(userInfoDomain, Integer.parseInt(expire));
        String url = urlTemplate.replace("##token##", token);
        // 生成时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = format.format(new Date());
        // 渲染 username url sendTime
        content = content.replaceAll("##username##", userInfoDomain.getUsername())
                .replaceAll("##url##", url)
                .replaceAll("##send_time##", sendTime);
        MailSender.send(Collections.singletonList(userInfoDomain.getEmail()), subject, content);
        return StatusMsgConstant.SUCCESS;
    }

    @Override
    public void resetPassword(Context context, String password) {
        UserInfoDomain domain = new UserInfoDomain();
        domain.setId(context.getOperatorId());
        domain.setPassword(Md5Util.StringInMd5(password));
        userInfoRepo.update(domain);
    }

    @Override
    public SingleResult<Integer> queryTotal(Context context) {
        return new SingleResult<>(userInfoRepo.queryTotal());
    }

    @Override
    public PaginationResult<UserInfo> submitStatistic(Context context, PageQuery pageQuery, TimeType timeType) {
        List<UserRankDomain> ranks = userRankRepo.queryPageByTimeType(timeType, pageQuery);
        Integer total = userRankRepo.total(timeType);
        PaginationResult<UserInfo> result = new PaginationResult<>();
        result.setTotal(total);
        if (ranks == null || ranks.size() == 0) {
            result.setPagingList(null);
            return result;
        }
        Map<Integer, UserRankDomain> rankDomainMap = ranks.stream().collect(Collectors.toMap(UserRankDomain::getUserId, item -> item));
        // 用户ids
        List<Integer> userIds = new ArrayList<>(rankDomainMap.keySet());
        List<UserInfoDomain> userInfoDomains = userInfoRepo.queryByIds(userIds, false);
        List<UserInfo> userInfos = new ArrayList<>(userInfoDomains.size());
        userInfoDomains.forEach(item -> {
            UserRankDomain rankDomain = rankDomainMap.get(item.getId());
            UserSubmitInfoDomain submitInfoDomain = new UserSubmitInfoDomain();
            submitInfoDomain.setAcNum(rankDomain.getAcNum());
            submitInfoDomain.setSubmitNum(rankDomain.getSubmitNum());
            item.setSubmitInfo(submitInfoDomain);
            userInfos.add(UserInfoConvert.domainToInfo(item));
        });
        result.setPagingList(userInfos);
        return result;
    }

    private LoginResult loginSuccess(UserInfoDomain domain) {
        LoginResult result = new LoginResult();
        result.setStatus(StatusMsgConstant.LOGIN_SUCCESS);
        result.setAvatar(domain.getAvatar());
        result.setUsername(domain.getUsername());
        result.setRoleId(domain.getRoleId());
        result.setUserId(domain.getId());
        userLoginLogRepo.save(new UserLoginLogModel(null, domain.getId(), System.currentTimeMillis()));
        onlineService.online(result.getUserId());
        return result;
    }

    private String tokenForResetPwd(UserInfoDomain domain, int exp) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserInfoConstant.ROLE_ID, domain.getRoleId());
        claims.put(UserInfoConstant.USER_ID, domain.getId());
        claims.put(UserInfoConstant.USERNAME, domain.getUsername());
        return TokenUtil.tokens(claims, AppSetting.secretKey, exp, AppSetting.jwtIssuer, AppSetting.jwtAud);
    }

    private Map<Integer, String> getSiteSetting() {
        List<SiteSettingDomain> domains = siteSettingRepo.queryByIds(
                Arrays.asList(SettingsConstant.RESET_PWD_CONTENT,
                        SettingsConstant.RESET_PWD_SUBJECT,
                        SettingsConstant.RESET_PWD_FRONT_END_URL,
                        SettingsConstant.RESET_PWD_MAIL_EXP));
        return domains.stream().collect(Collectors.toMap(SiteSettingDomain::getId, SiteSettingDomain::getValue));
    }
}
