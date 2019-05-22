package com.cafe24.mysite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.CommentDao;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;

@Service
public class CommentService {
	
	@Autowired
	CommentDao commentDao;
	
	public boolean commentWrite(CommentVo commentVo, UserVo authUser) {
		commentVo.setUserNo(authUser.getNo());
		
		//첫글이면
		if(commentVo.getGroupNo() == -1) {
			commentVo.setOrderNo(1);
			commentVo.setDepth(0);
		}else {
			//답글이면
			commentVo.setOrderNo(commentVo.getOrderNo()+1);
			commentVo.setDepth(commentVo.getDepth()+1);
			commentDao.updateOrderNo(commentVo);
		}
		
		return commentDao.insert(commentVo);
	}
	
	public List<CommentVo> getList(Long boardNo) {
		List<CommentVo> list = commentDao.getList(boardNo);
		
		for(CommentVo vo : list) {
			vo.setContents(vo.getContents().replaceAll("\\n", "<br />"));
		}
		
		return list;
	}
	
	public boolean commentDelete(CommentVo commentVo, UserVo authUser) {
		CommentVo cvo = commentDao.getByNo(commentVo.getNo());
		
		if(cvo.getUserNo() != authUser.getNo())
			return false;
		
		return commentDao.delete(commentVo.getNo());
	}
}
