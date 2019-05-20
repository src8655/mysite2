package com.cafe24.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVo;

@Repository
public class BoardDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public boolean insert(BoardVo boardVo) {
		int result = sqlSession.update("board.insert", boardVo);
		return result == 1;
	}
	
	public List<BoardVo> getList() {
		return sqlSession.selectList("board.getList");
	}
	
	public BoardVo getByNo(Long no) {
		return (BoardVo)sqlSession.selectOne("board.getByNo", no);
	}
}
