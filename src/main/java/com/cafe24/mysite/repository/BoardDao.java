package com.cafe24.mysite.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVo;

@Repository
public class BoardDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public Long insert(BoardVo boardVo) {
		int result = sqlSession.insert("board.insert", boardVo);
		return boardVo.getNo();
	}
	public boolean updateOrderNo(BoardVo boardVo) {
		int result = sqlSession.update("board.updateOrderNo", boardVo);
		return result == 1;
	}
	
	public List<BoardVo> getList(Map daoMap) {
		return sqlSession.selectList("board.getList", daoMap);
	}
	
	public BoardVo getByNo(Long no) {
		return (BoardVo)sqlSession.selectOne("board.getByNo", no);
	}
	
	public boolean update(BoardVo boardVo) {
		int result = sqlSession.update("board.update", boardVo);
		return result == 1;
	}
	
	public boolean delete(Long no) {
		int result = sqlSession.delete("board.delete", no);
		return result == 1;
	}
	
	public int getCount(String kwd) {
		return (Integer)sqlSession.selectOne("board.count", kwd);
	}
	
	public boolean updateHit(Long no) {
		int result = sqlSession.update("board.updateHit", no);
		return result == 1;
	}
	
	public int countchild(Long no) {
		return (Integer)sqlSession.selectOne("board.countchild", no);
	}
	public boolean setdelmode(Long no) {
		int result = sqlSession.update("board.setdelmode", no);
		return result == 1;
	}
}
