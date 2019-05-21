package com.cafe24.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.UserDao;
import com.cafe24.mysite.vo.UserVo;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	public boolean existEmail(String email) {
		UserVo userVo = userDao.get(email);
		return userVo != null;
	}
	
	public boolean join(UserVo userVo) {
		return userDao.insert(userVo);
	}
	
	public UserVo getUser(UserVo userVo) {
		return userDao.get(userVo);
	}
	
	public UserVo getUser(Long no) {
		return userDao.get(no);
	}
	
	public boolean update(UserVo userVo, UserVo oldUserVo) {

		oldUserVo = userDao.get(oldUserVo.getNo());
		
		oldUserVo.setName(userVo.getName());
		oldUserVo.setGender(userVo.getGender());
		if(userVo.getPassword() != null && !"".equals(userVo.getPassword()))
			oldUserVo.setPassword(userVo.getPassword());
		
		return userDao.update(oldUserVo);
	}
	
	
}
