package com.kelab.usercenter.dal.model;

public class NewsRollModel {
	private Integer id;

	private String content;

	private Long pubTime;

	private Boolean active;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getPubTime() {
		return pubTime;
	}

	public void setPubTime(Long pubTime) {
		this.pubTime = pubTime;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}