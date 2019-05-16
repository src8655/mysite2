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
	
	public List<BoardVo> getList(String kwd) {
		return boardDao.getList(kwd);
	}
	
	public boolean insert(BoardVo boardVo) {
		boardVo.setHit(0L);
		return boardDao.insert(boardVo);
	}
	
	public boolean delete(Long no, UserVo authUser) {
		BoardVo boardVo = boardDao.selectOne(no);
		
		//내 글만 수정
		if(authUser.getNo() != boardVo.getUserNo())
			return false;
		
		return boardDao.delete(no);
	}
	
	public BoardVo selectOne(Long no) {
		return boardDao.selectOne(no);
	}

	public boolean update(BoardVo boardVo, UserVo userVo) {
		BoardVo oldBoardVo = boardDao.selectOne(boardVo.getNo());
		
		if(userVo.getNo() != oldBoardVo.getUserNo())
			return false;
		
		oldBoardVo.setSubject(boardVo.getSubject());
		oldBoardVo.setContents(boardVo.getContents());
		
		return boardDao.update(oldBoardVo);
	}
}
