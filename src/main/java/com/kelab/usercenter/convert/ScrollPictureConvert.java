package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.ScrollPictureInfo;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;
import com.kelab.usercenter.dal.model.ScrollPictureModel;
import org.springframework.beans.BeanUtils;

public class ScrollPictureConvert {
	public static ScrollPictureDomain modelToDomain(ScrollPictureModel model) {
		if (model == null) {
			return null;
		}
		ScrollPictureDomain domain = new ScrollPictureDomain();
		BeanUtils.copyProperties(model, domain);
		return domain;
	}

	public static ScrollPictureModel domainToModel(ScrollPictureDomain domain) {
		if (domain == null) {
			return null;
		}
		ScrollPictureModel model = new ScrollPictureModel();
		BeanUtils.copyProperties(domain, model);
		return model;
	}

	public static ScrollPictureInfo domainToInfo(ScrollPictureDomain domain) {
		if (domain == null) {
			return null;
		}
		ScrollPictureInfo info = new ScrollPictureInfo();
		BeanUtils.copyProperties(domain, info);
		return info;
	}

	public static ScrollPictureDomain infoToDomain(ScrollPictureInfo info) {
		if (info == null) {
			return null;
		}
		ScrollPictureDomain domain = new ScrollPictureDomain();
		BeanUtils.copyProperties(info, domain);
		return domain;
	}
}