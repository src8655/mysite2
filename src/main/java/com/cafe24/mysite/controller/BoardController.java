package com.cafe24.mysite.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;

	@RequestMapping("/list")
	public String list(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			Model model
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		model.addAttribute("pages", pages);

		Map<String, Object> map = boardService.getList(pages, kwd_decode);
		
		List<BoardVo> list = (List<BoardVo>)map.get("list");
		Map<String, Integer> pagingMap = (Map<String, Integer>)map.get("pagingMap");
		model.addAttribute("list", list);
		model.addAttribute("pagingMap", pagingMap);
		model.addAttribute("cnt", 0);
		
		return "board/list";
	}
	
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public String write(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") int no,
			@RequestParam(value="groupNo", required = true, defaultValue = "-1") int groupNo,
			@RequestParam(value="orderNo", required = true, defaultValue = "-1") int orderNo,
			@RequestParam(value="depth", required = true, defaultValue = "-1") int depth,
			Model model,
			HttpSession session
			) {
		model.addAttribute("pages", pages);
		
		if(session == null) 
			if(no == -1) return "redirect:/board/list";
			else return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) 
			if(no == -1) return "redirect:/board/list";
			else return "redirect:/board/list";

		model.addAttribute("groupNo", groupNo);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("depth", depth);
		model.addAttribute("no", no);
		
		return "board/write";
	}
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) {
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		boardService.boardWrite(boardVo, authUser);
		
		if(no == -1) return "redirect:/board/list";
		else return "redirect:/board/list?pages="+pages;
	}
	

	@RequestMapping("/view")
	public String view(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			@CookieValue(value="mysite_board_hit", required = true, defaultValue = "-1") String cookie,
			HttpServletResponse response,
			Model model
			) {
		model.addAttribute("pages", pages);
		System.out.println(cookie + "**************************** cookie");
		
		//이미 있으면 null을 반환
		cookie = boardService.updateHit(no, cookie);
		if(cookie != null) {
			Cookie co = new Cookie("mysite_board_hit", cookie);
			co.setMaxAge(60*60*24);
			response.addCookie(co);
		}
		
		BoardVo boardVo = boardService.getOne(no);
		model.addAttribute("boardVo", boardVo);
		
		return "board/view";
	}

	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public String modify(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			Model model,
			HttpSession session
			) {
		model.addAttribute("pages", pages);
		
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		BoardVo boardVo = boardService.getOne(no);
		if(authUser.getNo() != boardVo.getUserNo()) return "redirect:/board/list";
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/modify";
	}
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) {
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		BoardVo boardVoOld = boardService.getOne(boardVo.getNo());
		if(authUser.getNo() != boardVoOld.getUserNo()) return "redirect:/board/list";
		
		boardService.modify(boardVo);
		
		
		return "redirect:/board/view?no="+boardVo.getNo() + "&pages="+pages;
	}
	
	@RequestMapping("/delete")
	public String delete(
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			HttpSession session
			) {
		
		 if(session == null) return "redirect:/board/list";
		 UserVo authUser = (UserVo)session.getAttribute("authUser");
		 if(authUser == null) return "redirect:/board/list";
		 if(!boardService.delOne(no, authUser)) return "redirect:/board/list";
		
		return "redirect:/board/list?pages="+pages;
	}

}
