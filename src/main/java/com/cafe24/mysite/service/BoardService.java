package com.cafe24.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.BoardDao;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Service
public class BoardService {

	@Autowired
	BoardDao boardDao;
	
	public boolean boardWrite(BoardVo boardVo, UserVo authUser) {
		boardVo.setUserNo(authUser.getNo());
		boardVo.setHit(0);
		
		//첫글이면
		if(boardVo.getGroupNo() == -1) {
			boardVo.setOrderNo(1);
			boardVo.setDepth(0);
		}else {
			//답글이면
			boardVo.setOrderNo(boardVo.getOrderNo()+1);
			boardVo.setDepth(boardVo.getDepth()+1);
		}
		
		return boardDao.insert(boardVo);
	}
	
	public List<BoardVo> getList() {
		return boardDao.getList();
	}
	
	public BoardVo getOne(Long no) {
		return boardDao.getByNo(no);
	}
}
