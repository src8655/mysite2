package com.cafe24.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.BoardDao;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Service
public class BoardService {
	public static final int BOARD_CNT = 6;	//한번에 보여질 게시글
	public static final int PAGE_CNT = 5;	//페이지 버튼 개수

	@Autowired
	BoardDao boardDao;
	
	public Long boardWrite(BoardVo boardVo, UserVo authUser) {
		boardVo.setUserNo(authUser.getNo());
		boardVo.setHit(0);
		boardVo.setStatus(1);
		
		//첫글이면
		if(boardVo.getGroupNo() == -1) {
			boardVo.setOrderNo(1);
			boardVo.setDepth(0);
		}else {
			//답글이면
			boardVo.setOrderNo(boardVo.getOrderNo()+1);
			boardVo.setDepth(boardVo.getDepth()+1);
			boardVo.setParentNo(boardVo.getNo());
			boardDao.updateOrderNo(boardVo);
		}
		
		return boardDao.insert(boardVo);
	}
	
	public Map<String, Object> getList(int pages, String kwd) {
		int count = boardDao.getCount(kwd);	//총 게시글 개수
		int lastPage = (int) Math.ceil((double)count/(double)BOARD_CNT);	//마지막 페이지
		int startNum = ((pages-1) * BOARD_CNT);		//시작번호
		int rangeStart = ((pages-1)/PAGE_CNT) * PAGE_CNT + 1;		//페이지 범위

		Map<String, Integer> pagingMap = new HashMap<String, Integer>();
		pagingMap.put("count", count);
		pagingMap.put("lastPage", lastPage);
		pagingMap.put("startNum", startNum);
		pagingMap.put("rangeStart", rangeStart);
		pagingMap.put("boardCnt", BOARD_CNT);
		pagingMap.put("pageCnt", PAGE_CNT);
		
		Map<String, Object> daoMap = new HashMap<String, Object>();
		daoMap.put("startNum", startNum);
		daoMap.put("boardCnt", BOARD_CNT);
		daoMap.put("kwd", kwd);
		
		List<BoardVo> list = boardDao.getList(daoMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("pagingMap", pagingMap);
		
		return map;
	}
	
	public BoardVo getOne(Long no) {
		BoardVo boardVo = boardDao.getByNo(no);
		boardVo.setContents(boardVo.getContents().replaceAll("\\n", "<br />"));
		return boardVo;
	}
	public BoardVo getOneModify(Long no) {
		return boardDao.getByNo(no);
	}
	
	public boolean modify(BoardVo boardVo) {
		return boardDao.update(boardVo);
	}
	
	public boolean delOne(Long no, UserVo authUser) {
		BoardVo boardVo = boardDao.getByNo(no);
		if(authUser.getNo() != boardVo.getUserNo())
			return false;
		
		boolean result = false;
		
		//자식 개수 확인
		int countChild = boardDao.countchild(no);
		//자식이 있으면 상태만 삭제로 변경
		if(countChild != 0) {
			result = boardDao.setdelmode(no);
		}else {
			//자식이 없으면 본인것을 삭제
			result = boardDao.delete(no);
			//그리고 자식이 없고 상태가 -1인 것을 찾아 삭제
			List<BoardVo> list = boardDao.finddel();
			for(BoardVo vo : list) {
				boardDao.delete(vo.getNo());
			}
		}
		
		
		
		return result;
	}
	
	public String updateHit(Long no, String cookie) {
		//쿠키처리
		String[] split = cookie.split("//");
		boolean isContainCookie = false;
		for(int i=0;i<split.length;i++) {
			Long tmp = Long.parseLong(split[i]);
			if(no == tmp) isContainCookie = true;
		}
		//쿠키에 없으면
		if(!isContainCookie) {
			boardDao.updateHit(no);
			cookie += ("//" + no);
		}else {
			cookie = null;
		}
		
		return cookie;
	}
}
