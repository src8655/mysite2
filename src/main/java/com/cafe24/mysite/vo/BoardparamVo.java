package com.cafe24.mysite.vo;

public class BoardparamVo {
	private int groupNo = -1;
	private int orderNo = -1;
	private int depth = -1;
	private Long no = -1L;
	private int pages = 1;
	private String kwd = "";
	private String kwd_decode = "";
	private String kwd_encode = "";
	
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
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getKwd() {
		return kwd;
	}
	public void setKwd(String kwd) {
		this.kwd = kwd;
	}
	public String getKwd_decode() {
		return kwd_decode;
	}
	public void setKwd_decode(String kwd_decode) {
		this.kwd_decode = kwd_decode;
	}
	public String getKwd_encode() {
		return kwd_encode;
	}
	public void setKwd_encode(String kwd_encode) {
		this.kwd_encode = kwd_encode;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	
}
