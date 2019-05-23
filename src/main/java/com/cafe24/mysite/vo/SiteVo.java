package com.cafe24.mysite.vo;

import org.hibernate.validator.constraints.NotEmpty;

public class SiteVo {
	private Long no;
	@NotEmpty
	private String title;
	@NotEmpty
	private String welcomeMessage;
	private String profile;
	@NotEmpty
	private String description;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
