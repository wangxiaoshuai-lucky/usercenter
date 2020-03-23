package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.NewsInfo;
import com.kelab.usercenter.dal.domain.NewsDomain;
import com.kelab.usercenter.dal.model.NewsModel;
import org.springframework.beans.BeanUtils;

public class NewsConvert {
	public static NewsDomain modelToDomain(NewsModel model) {
		if (model == null) {
			return null;
		}
		NewsDomain domain = new NewsDomain();
		BeanUtils.copyProperties(model, domain);
		return domain;
	}

	public static NewsModel domainToModel(NewsDomain domain) {
		if (domain == null) {
			return null;
		}
		NewsModel model = new NewsModel();
		BeanUtils.copyProperties(domain, model);
		return model;
	}

	public static NewsInfo domainToInfo(NewsDomain domain) {
		if (domain == null) {
			return null;
		}
		NewsInfo info = new NewsInfo();
		BeanUtils.copyProperties(domain, info);
		info.setPublishUserInfo(UserInfoConvert.domainToInfo(domain.getPublishUserInfo()));
		return info;
	}

	public static NewsDomain infoToDomain(NewsInfo info) {
		if (info == null) {
			return null;
		}
		NewsDomain domain = new NewsDomain();
		BeanUtils.copyProperties(info, domain);
		return domain;
	}
}