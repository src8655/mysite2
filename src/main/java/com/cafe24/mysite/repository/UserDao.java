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
	
	public boolean update(UserVo vo) {
		boolean result = false;
		
		int count = sqlSession.update("userUpdate", vo);
		if(count == 1) result = true;
		
		return result;
	}
	public UserVo get(Long no) {
		UserVo result = null;
		
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("no", no);
		
		result = sqlSession.selectOne("userGetByNo", map);
		
		return result;
	}
	public UserVo get(UserVo userVo) {
		UserVo result = null;
		
		result = (UserVo)sqlSession.selectOne("userGetByEmailAndPassword", userVo);
		
		return result;
	}
	public boolean insert(UserVo vo) {
		boolean result = false;
		
		int count = sqlSession.insert("userInsert", vo);
		
		if(count == 1) result = true;
		
		return result;
	}
}
