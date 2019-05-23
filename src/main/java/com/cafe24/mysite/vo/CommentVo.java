package com.cafe24.mysite.vo;

import org.hibernate.validator.constraints.NotEmpty;

public class CommentVo {
	private Long no;
	@NotEmpty
	private String contents;
	private String regDate;
	private int groupNo;
	private int orderNo;
	private int depth;
	private Long userNo;
	private Long boardNo;
	private Long parentNo;
	private Integer status;
	
	private String userName;

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(Long boardNo) {
		this.boardNo = boardNo;
	}

	public Long getParentNo() {
		return parentNo;
	}

	public void setParentNo(Long parentNo) {
		this.parentNo = parentNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
