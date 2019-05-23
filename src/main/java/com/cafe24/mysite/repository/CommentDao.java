package com.cafe24.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.CommentVo;


@Repository
public class CommentDao {

	@Autowired
	SqlSession sqlSession;
	
	public boolean insert(CommentVo commentVo) {
		int result = sqlSession.insert("comment.insert", commentVo);
		return result == 1;
	}
	public boolean updateOrderNo(CommentVo commentVo) {
		int result = sqlSession.update("comment.updateOrderNo", commentVo);
		return result == 1;
	}
	public List<CommentVo> getList(Long boardNo) {
		return sqlSession.selectList("comment.getList", boardNo);
	}
	public boolean delete(Long no) {
		int result = sqlSession.delete("comment.delete", no);
		return result == 1;
	}
	public CommentVo getByNo(Long no) {
		return (CommentVo)sqlSession.selectOne("comment.getByNo", no);
	}
	
	public int countchild(Long no) {
		return (Integer)sqlSession.selectOne("comment.countchild", no);
	}
	public boolean setdelmode(Long no) {
		int result = sqlSession.update("comment.setdelmode", no);
		return result == 1;
	}
}
