package com.cafe24.mysite.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.UserVo;

@Repository
public class UserDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public UserVo get(String email) {
		UserVo result = null;
		
		result = (UserVo)sqlSession.selectOne("user.getByEmail", email);
		
		return result;
	}
	
	public boolean update(UserVo vo) {
		boolean result = false;
		
		int count = sqlSession.update("user.update", vo);
		if(count == 1) result = true;
		
		return result;
	}
	public UserVo get(Long no) {
		UserVo result = null;
		
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("no", no);
		
		result = sqlSession.selectOne("user.getByNo", map);
		
		return result;
	}
	public UserVo get(UserVo userVo) {
		UserVo result = null;
		
		result = (UserVo)sqlSession.selectOne("user.getByEmailAndPassword", userVo);
		
		return result;
	}
	public boolean insert(UserVo vo) {
		boolean result = false;
		
		int count = sqlSession.insert("user.insert", vo);
		
		if(count == 1) result = true;
		
		return result;
	}
}
