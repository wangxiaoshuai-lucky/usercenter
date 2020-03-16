package com.kelab.usercenter.serivce.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.JsonWebTokenConstant;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.base.PageQuery;
import com.kelab.info.base.query.usercenter.UserQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.builder.UserInfoBuilder;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.SettingsConstant;
import com.kelab.usercenter.constant.enums.CacheBizName;
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
import com.kelab.usercenter.result.LoginResult;
import com.kelab.usercenter.result.SingleResult;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.usercenter.support.ContextLogger;
import com.kelab.usercenter.support.MailSender;
import com.kelab.util.md5.Md5Util;
import com.kelab.util.token.TokenUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final ContextLogger log = new ContextLogger(UserInfoServiceImpl.class);

    private UserInfoRepo userInfoRepo;

    private UserRankRepo userRankRepo;

    private SiteSettingRepo siteSettingRepo;

    private RedisCache redisCache;

    private UserLoginLogRepo userLoginLogRepo;

    @Autowired(required = false)
    public UserInfoServiceImpl(UserInfoRepo userInfoRepo
            , UserRankRepo userRankRepo
            , SiteSettingRepo siteSettingRepo
            , RedisCache redisCache
            , UserLoginLogRepo userLoginLogRepo) {
        this.userInfoRepo = userInfoRepo;
        this.userRankRepo = userRankRepo;
        this.siteSettingRepo = siteSettingRepo;
        this.redisCache = redisCache;
        this.userLoginLogRepo = userLoginLogRepo;
    }

    @Override
    public LoginResult login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = new LoginResult();
        if (!verifyCode.equalsIgnoreCase(redisCache.get(CacheBizName.VERIFY_CODE, uuid))) {
            result.setStatus(StatusMsgConstant.VERIFY_CODE_ERROR);
            return result;
        }
        // 校验验证码后验证码失效
        redisCache.delete(CacheBizName.VERIFY_CODE, uuid);
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
        String originCode = redisCache.get(CacheBizName.VERIFY_CODE, uuid);
        if (!verifyCode.equalsIgnoreCase(originCode)) {
            return StatusMsgConstant.VERIFY_CODE_ERROR;
        }
        redisCache.delete(CacheBizName.VERIFY_CODE, uuid);
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
    public void resetPassword(Context context, Integer userId, String password) {
        UserInfoDomain domain = new UserInfoDomain();
        domain.setId(userId);
        domain.setPassword(Md5Util.StringInMd5(password));
        userInfoRepo.update(domain);
    }

    @Override
    public SingleResult<Integer> queryTotal(Context context) {
        return new SingleResult<>(userInfoRepo.queryTotal(new UserQuery()));
    }

    @Override
    public PaginationResult<UserInfo> submitStatistic(Context context, PageQuery pageQuery, TimeType timeType) {
        List<UserRankDomain> ranks = userRankRepo.queryPageByTimeType(timeType, pageQuery);
        Integer total = userRankRepo.total(timeType);
        PaginationResult<UserInfo> result = new PaginationResult<>();
        result.setTotal(total);
        if (CollectionUtils.isEmpty(ranks)) {
            return result;
        }
        List<Integer> userIds = ranks.stream().map(UserRankDomain::getUserId).collect(Collectors.toList());
        List<UserInfoDomain> userInfoDomains = userInfoRepo.queryByIds(userIds, false);
        Map<Integer, UserInfoDomain> userInfoDomainMap = userInfoDomains.stream().collect(Collectors.toMap(UserInfoDomain::getId, v -> v));
        List<UserInfo> userInfos = new ArrayList<>(ranks.size());
        ranks.forEach(item -> {
            UserInfoDomain info = userInfoDomainMap.get(item.getUserId());
            UserSubmitInfoDomain submitInfoDomain = new UserSubmitInfoDomain();
            submitInfoDomain.setAcNum(item.getAcNum());
            submitInfoDomain.setSubmitNum(item.getSubmitNum());
            submitInfoDomain.setRank(item.getRank());
            info.setSubmitInfo(submitInfoDomain);
            userInfos.add(UserInfoConvert.domainToInfo(info));
        });
        result.setPagingList(userInfos);
        return result;
    }

    @Override
    public PaginationResult<UserInfo> queryPage(Context context, UserQuery query) {
        List<Integer> ids = totalIds(query);
        PaginationResult<UserInfo> result = new PaginationResult<>();
        if (ids.size() != 0) {
            // 1. 通过 id ids 查询, 可以走缓存
            List<UserInfo> userInfos = this.queryByIds(context, ids, true);
            result.setPagingList(userInfos);
            result.setTotal(userInfos.size());
        } else {
            // 2. 通过其他信息查询
            List<UserInfoDomain> userInfoDomains = userInfoRepo.queryPage(query, true);
            List<UserInfo> userInfos = new ArrayList<>(userInfoDomains.size());
            userInfoDomains.forEach(item -> userInfos.add(UserInfoConvert.domainToInfo(item)));
            result.setPagingList(userInfos);
            result.setTotal(userInfoRepo.queryTotal(query));
        }
        return result;
    }

    @Override
    @Verify(sizeLimit = {"ids [1, 200]"})
    public List<UserInfo> queryByIds(Context context, List<Integer> ids, boolean withSubmitInfo) {
        List<UserInfoDomain> userInfoDomains = userInfoRepo.queryByIds(ids, withSubmitInfo);
        if (CollectionUtils.isEmpty(userInfoDomains)) {
            return Collections.emptyList();
        }
        List<UserInfo> userInfos = new ArrayList<>(userInfoDomains.size());
        userInfoDomains.forEach(item -> userInfos.add(UserInfoConvert.domainToInfo(item)));
        return userInfos;
    }

    @Override
    public String update(Context context, UserInfoDomain domain) {
        if (domain.getPassword() != null) {
            domain.setPassword(Md5Util.StringInMd5(domain.getPassword()));
        }
        // username 查重
        if (Strings.isNotBlank(domain.getUsername())) {
            UserInfoDomain old = userInfoRepo.queryByUsername(domain.getUsername(), false);
            if (old != null && !old.getId().equals(domain.getId())) {
                return StatusMsgConstant.USER_EXISTED_ERROR;
            }
        }
        // stuId 查重
        if (Strings.isNotBlank(domain.getStudentId())) {
            UserInfoDomain old = userInfoRepo.queryByStudentId(domain.getStudentId(), false);
            if (old != null && !old.getId().equals(domain.getId())) {
                return StatusMsgConstant.USER_EXISTED_ERROR;
            }
        }
        userInfoRepo.update(domain);
        return StatusMsgConstant.SUCCESS;
    }

    @Override
    @Verify(sizeLimit = {"ids [1, 200]"})
    public void delete(Context context, List<Integer> ids) {
        List<UserInfoDomain> oldUsers = userInfoRepo.queryByIds(ids, true);
        if (!CollectionUtils.isEmpty(oldUsers)) {
            userInfoRepo.delete(ids);
            log.info(context, "删除用户：{}", JSON.toJSONString(oldUsers));
        }
    }


    private List<Integer> totalIds(UserQuery query) {
        List<Integer> userIds = new ArrayList<>();
        if (query.getIds() != null) {
            userIds.addAll(query.getIds());
        }
        if (query.getId() != null) {
            userIds.add(query.getId());
        }
        return userIds;
    }

    private LoginResult loginSuccess(UserInfoDomain domain) {
        LoginResult result = new LoginResult();
        result.setStatus(StatusMsgConstant.LOGIN_SUCCESS);
        result.setAvatar(domain.getAvatar());
        result.setUsername(domain.getUsername());
        result.setRoleId(domain.getRoleId());
        result.setUserId(domain.getId());
        userLoginLogRepo.save(new UserLoginLogModel(null, domain.getId(), System.currentTimeMillis()));
        return result;
    }

    private String tokenForResetPwd(UserInfoDomain domain, int exp) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JsonWebTokenConstant.ROLE_ID, domain.getRoleId());
        claims.put(JsonWebTokenConstant.USER_ID, domain.getId());
        claims.put(JsonWebTokenConstant.USERNAME, domain.getUsername());
        claims.put(JsonWebTokenConstant.REFRESH_EXP_DATE, System.currentTimeMillis());
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
