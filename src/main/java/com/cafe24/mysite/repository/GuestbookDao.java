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
		
		result = sqlSession.selectList("guestbook.getlist");
		
		return result;
	}
	public boolean insert(GuestbookVo vo) {
		boolean result = false;

		int count = sqlSession.insert("guestbook.insert", vo);
		if(count == 1) result = true;
		
		return result;
	}

	public boolean delete(GuestbookVo vo) {
		boolean result = false;
		
		int count = sqlSession.delete("guestbook.delete", vo);
		if(count == 1) result = true;
		
		return result;
	}
	
}
