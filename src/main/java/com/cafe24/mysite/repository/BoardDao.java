package com.cafe24.mysite.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVo;


@Repository
public class BoardDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	
	public boolean delete(Long no) {
		boolean result = false;
		
		int count = sqlSession.delete("boardDelete", no);
		if(count == 1) result = true;
		
		return result;
	}
	public boolean update(BoardVo vo) {
		boolean result = false;
		
		int count = sqlSession.update("boardUpdate", vo);
		if(count == 1) result = true;
		
		return result;
	}
	public BoardVo selectOne(Long no) {
		BoardVo result = new BoardVo();
		
		result = sqlSession.selectOne("boardSelectOne", no);
		
		return result;
	}
	public List<BoardVo> getList(String kwd) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		result = sqlSession.selectList("boardGetList", kwd);
		
		return result;
	}
	public boolean insert(BoardVo vo) {
		boolean result = false;
		
		int count = sqlSession.insert("boardInsert", vo);
		if(count == 1) result = true;
		
		return result;
	}
}
