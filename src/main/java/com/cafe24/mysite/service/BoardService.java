package com.cafe24.mysite.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.repository.BoardDao;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Service
public class BoardService {
	public static final int BOARD_CNT = 6;	//한번에 보여질 게시글
	public static final int PAGE_CNT = 5;	//페이지 버튼 개수

	private static final String SAVE_PATH = "/mysite-uploads";
	private static final String URL = "/images";

	@Autowired
	BoardDao boardDao;
	
	public Long boardWrite(BoardVo boardVo, UserVo authUser, MultipartFile files1) {
		boardVo.setUserNo(authUser.getNo());
		boardVo.setHit(0);
		boardVo.setStatus(1);
		
		String url = restore(files1);
		boardVo.setFile1(url);
		
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
	
	public boolean modify(BoardVo boardVo, UserVo authUser, MultipartFile files1) {
		
		BoardVo boardVoOld = boardDao.getByNo(boardVo.getNo());
		if(boardVoOld == null) return false;
		if(authUser.getNo() != boardVoOld.getUserNo()) return false;
		
		boardVoOld.setTitle(boardVo.getTitle());
		boardVoOld.setContents(boardVo.getContents());
		
		if(boardVo.getCheckdel() == 1) {
			boardVoOld.setFile1("");
		}else {
			String url = restore(files1);
			if(!url.equals("")) boardVoOld.setFile1(url);
		}
		
		return boardDao.update(boardVoOld);
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
			//나한테 부모가 있는지 확인
			if(boardVo.getParentNo() != null) {
				//부모가 있으면 위로 올라감
				delrepeat(boardVo.getParentNo());
			}
		}
		
		
		
		return result;
	}
	public void delrepeat(Long no) {
		BoardVo boardVo = boardDao.getByNo(no);
		//상태가 삭제 상태면
		if(boardVo.getStatus() == -1) {
			//자식 개수 확인
			int countChild = boardDao.countchild(no);
			if(countChild == 0) {
				//자식이 없으면 삭제
				boardDao.delete(no);
				//나한테 부모가 있는지 확인
				if(boardVo.getParentNo() != null) {
					//부모가 있으면 위로 올라감
					delrepeat(boardVo.getParentNo());
				}
			}
		}
	}
	
	public String updateHit(Long no, String cookie) {
		//System.out.println(cookie);
		
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
	
	
	
	public String restore(MultipartFile multipartFile) {
		String url = "";

		try {
		
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originalFilename = 
					multipartFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
			String saveFileName = generateSaveFileName(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("##########" + originalFilename);
			System.out.println("##########" + extName);
			System.out.println("##########" + saveFileName);
			System.out.println("##########" + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
			os.write(fileData);
			os.close();
			
			

			//multipartFile.transferTo(new File(SAVE_PATH + "/" + saveFileName));
			
			url = URL + "/" + saveFileName;
			
		} catch (IOException e) {
			throw new RuntimeException("Fileupload error:" + e);
		}
		
		return url;
	}
	
	private String generateSaveFileName(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);
		
		return filename;
	}
}
