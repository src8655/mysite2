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
import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.BoardparamVo;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	CommentService commentService;

	@RequestMapping("/list")
	public String list(
			@ModelAttribute("bpv") BoardparamVo bpv,
			Model model
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));

		Map<String, Object> map = boardService.getList(bpv.getPages(), bpv.getKwd_decode());
		
		List<BoardVo> list = (List<BoardVo>)map.get("list");
		Map<String, Integer> pagingMap = (Map<String, Integer>)map.get("pagingMap");
		model.addAttribute("list", list);
		model.addAttribute("pagingMap", pagingMap);
		model.addAttribute("cnt", 0);
		
		return "board/list";
	}
	
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public String write(
			@ModelAttribute("bpv") BoardparamVo bpv,
			HttpSession session
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		if(session == null) 
			if(bpv.getNo() == -1) return "redirect:/board/list";
			else return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) 
			if(bpv.getNo() == -1) return "redirect:/board/list";
			else return "redirect:/board/list";
		
		return "board/write";
	}
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		Long newNo = boardService.boardWrite(boardVo, authUser);
		
		return "redirect:/board/view?no="+newNo+"&pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}
	

	@RequestMapping("/view")
	public String view(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			@CookieValue(value="mysite_board_hit", required = true, defaultValue = "-1") String cookie,
			HttpServletResponse response,
			Model model
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
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
		
		
		
		//댓글
		List<CommentVo> commentList = commentService.getList(no);
		model.addAttribute("commentList", commentList);
		
		return "board/view";
	}

	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public String modify(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			Model model,
			HttpSession session
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		model.addAttribute("pages", pages);
		
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		BoardVo boardVo = boardService.getOneModify(no);
		if(authUser.getNo() != boardVo.getUserNo()) return "redirect:/board/list";
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/modify";
	}
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		BoardVo boardVoOld = boardService.getOne(boardVo.getNo());
		if(authUser.getNo() != boardVoOld.getUserNo()) return "redirect:/board/list";
		
		boardService.modify(boardVo);
		
		
		return "redirect:/board/view?no="+boardVo.getNo() + "&pages="+pages+"&kwd="+kwd_encode;
	}
	
	@RequestMapping("/delete")
	public String delete(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			HttpSession session
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		
		 if(session == null) return "redirect:/board/list";
		 UserVo authUser = (UserVo)session.getAttribute("authUser");
		 if(authUser == null) return "redirect:/board/list";
		 if(!boardService.delOne(no, authUser)) return "redirect:/board/list";
		
		return "redirect:/board/list?pages="+pages+"&kwd="+kwd_encode;
	}

}
