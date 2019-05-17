package com.cafe24.mysite.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<GuestbookVo> getList() {
		List<GuestbookVo> result = new ArrayList<GuestbookVo>();
		
		result = sqlSession.selectList("guestbookGetlist");
		
		return result;
	}
	public boolean insert(GuestbookVo vo) {
		boolean result = false;

		int count = sqlSession.insert("guestbookInsert", vo);
		if(count == 1) result = true;
		
		return result;
	}

	public boolean delete(GuestbookVo vo) {
		boolean result = false;
		
		int count = sqlSession.delete("guestbookDelete", vo);
		if(count == 1) result = true;
		
		return result;
	}
	
}
