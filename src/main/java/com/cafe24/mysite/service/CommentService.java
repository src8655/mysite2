package com.cafe24.mysite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.CommentDao;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;

@Service
public class CommentService {
	
	@Autowired
	CommentDao commentDao;
	
	public boolean commentWrite(CommentVo commentVo, UserVo authUser) {
		commentVo.setUserNo(authUser.getNo());
		commentVo.setStatus(1);
		
		//첫글이면
		if(commentVo.getGroupNo() == -1) {
			commentVo.setOrderNo(1);
			commentVo.setDepth(0);
		}else {
			//답글이면
			commentVo.setOrderNo(commentVo.getOrderNo()+1);
			commentVo.setDepth(commentVo.getDepth()+1);
			commentVo.setParentNo(commentVo.getNo());
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
		
		boolean result = false;
		
		//자식 개수 확인
		int countChild = commentDao.countchild(commentVo.getNo());
		//자식이 있으면 상태만 삭제로 변경
		if(countChild != 0) {
			result = commentDao.setdelmode(commentVo.getNo());
		}else {
			//자식이 없으면 본인것을 삭제
			result = commentDao.delete(commentVo.getNo());
			//나한테 부모가 있는지 확인
			if(cvo.getParentNo() != null) {
				//부모가 있으면 위로 올라감
				delrepeat(cvo.getParentNo());
			}
		}
		
		return result;
	}

	public void delrepeat(Long no) {
		CommentVo commentVo = commentDao.getByNo(no);
		//상태가 삭제 상태면
		if(commentVo.getStatus() == -1) {
			//자식 개수 확인
			int countChild = commentDao.countchild(no);
			if(countChild == 0) {
				//자식이 없으면 삭제
				commentDao.delete(no);
				//나한테 부모가 있는지 확인
				if(commentVo.getParentNo() != null) {
					//부모가 있으면 위로 올라감
					delrepeat(commentVo.getParentNo());
				}
			}
		}
	}
}
