package com.cafe24.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.GuestbookDao;
import com.cafe24.mysite.vo.GuestbookVo;


@Service
public class GuestbookService {
	
	@Autowired
	GuestbookDao guestbookDao;
	
	public List<GuestbookVo> getList() {
		return guestbookDao.getList();
	}
	
	public boolean add(GuestbookVo guestbookVo) {
		return guestbookDao.insert(guestbookVo);
	}
	
	public boolean delete(GuestbookVo guestbookVo) {
		return guestbookDao.delete(guestbookVo);
	}
}
