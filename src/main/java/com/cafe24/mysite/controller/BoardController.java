package com.cafe24.mysite.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			Model model
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		
		List<BoardVo> list = boardService.getList(kwd);
		model.addAttribute("list", list);
		model.addAttribute("cnt", list.size());
		
		return "board/list";
	}
	

	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			HttpSession session,
			Model model
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		
		if(session == null) return "redirect:/board/list?kwd="+kwd_encode;
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list?kwd="+kwd_encode;
		
		model.addAttribute("kwd", kwd);
		
		return "board/write";
	}

	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) {
		
		if(session == null) return "redirect:/board/list?kwd="+kwd;
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list?kwd="+kwd;
		boardVo.setUserNo(authUser.getNo());
		
		boardService.insert(boardVo);
		
		return "redirect:/board/list";
	}

	@RequestMapping("/delete")
	public String delete(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			@RequestParam(value="no", required=true, defaultValue="-1") Long no,
			HttpSession session
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		
		if(session == null) return "redirect:/board/list?kwd="+kwd_encode;
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list?kwd="+kwd_encode;
		
		boardService.delete(no, authUser);
		
		return "redirect:/board/list?kwd="+kwd_encode;
	}
	

	@RequestMapping("/view")
	public String view(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			@RequestParam(value="no", required=true, defaultValue="-1") Long no,
			Model model
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		
		BoardVo boardVo = boardService.selectOne(no);
		boardVo.setContents(boardVo.getContents().replaceAll("\\n", "<br />"));
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/view";
	}
	
	

	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			@RequestParam(value="no", required=true, defaultValue="-1") Long no,
			HttpSession session,
			Model model
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		model.addAttribute("kwd_decode", kwd_decode);
		model.addAttribute("kwd_encode", kwd_encode);
		
		if(session == null) return "redirect:/board/view?kwd="+kwd_encode+"&no="+no;
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/view?kwd="+kwd_encode+"&no="+no;
		
		BoardVo boardVo = boardService.selectOne(no);
		
		if(boardVo.getUserNo() != authUser.getNo())
			return "redirect:/board/view?kwd="+kwd_encode+"&no="+no;

		model.addAttribute("boardVo", boardVo);
		model.addAttribute("kwd", kwd);
		
		return "board/modify";
	}

	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd,
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) throws UnsupportedEncodingException {
		
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		
		if(session == null) return "redirect:/board/view?kwd="+kwd_encode+"&no="+boardVo.getNo();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/view?kwd="+kwd_encode+"&no="+boardVo.getNo();
		
		boardService.update(boardVo, authUser);
		
		return "redirect:/board/view?kwd="+kwd_encode+"&no="+boardVo.getNo();
	}
}
