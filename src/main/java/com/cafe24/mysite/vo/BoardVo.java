package com.cafe24.mysite.vo;

import org.hibernate.validator.constraints.NotEmpty;

public class BoardVo {
	private Long no;
	@NotEmpty
	private String title;
	@NotEmpty
	private String contents;
	private Integer hit;
	private String regDate;
	private Integer groupNo;
	private Integer orderNo;
	private Integer depth;
	private Long userNo;
	private Long parentNo;
	private String file1;
	private Integer status;
	
	private Integer commentCount;
	private String userName;
	private Integer checkdel = -1;

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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public Integer getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getParentNo() {
		return parentNo;
	}

	public void setParentNo(Long parentNo) {
		this.parentNo = parentNo;
	}

	public Integer getCheckdel() {
		return checkdel;
	}

	public void setCheckdel(Integer checkdel) {
		this.checkdel = checkdel;
	}

	
}
