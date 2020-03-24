package com.kelab.usercenter.dal.domain;

public class AboutDomain {
	private Integer id;

	private String aboutName;

	private String aboutTitle;

	private String aboutContent;

	private String aboutIcon;

	private Integer aboutOrder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAboutName() {
		return aboutName;
	}

	public void setAboutName(String aboutName) {
		this.aboutName = aboutName;
	}

	public String getAboutTitle() {
		return aboutTitle;
	}

	public void setAboutTitle(String aboutTitle) {
		this.aboutTitle = aboutTitle;
	}

	public String getAboutContent() {
		return aboutContent;
	}

	public void setAboutContent(String aboutContent) {
		this.aboutContent = aboutContent;
	}

	public String getAboutIcon() {
		return aboutIcon;
	}

	public void setAboutIcon(String aboutIcon) {
		this.aboutIcon = aboutIcon;
	}

	public Integer getAboutOrder() {
		return aboutOrder;
	}

	public void setAboutOrder(Integer aboutOrder) {
		this.aboutOrder = aboutOrder;
	}
}