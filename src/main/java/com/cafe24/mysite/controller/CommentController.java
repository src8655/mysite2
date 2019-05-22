package com.cafe24.mysite.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/board/comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@ModelAttribute CommentVo commentVo,
			HttpSession session
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		commentService.commentWrite(commentVo, authUser);
		
		return "redirect:/board/view?no="+commentVo.getBoardNo()+"&pages="+pages+"&kwd="+kwd_encode;
	}
	
	@RequestMapping("/delete")
	public String delete(
			@RequestParam(value="kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(value="pages", required = true, defaultValue = "1") int pages,
			@ModelAttribute CommentVo commentVo,
			HttpSession session
			) throws UnsupportedEncodingException {
		String kwd_decode = URLDecoder.decode(kwd, "utf-8");
		String kwd_encode = URLEncoder.encode(kwd_decode, "utf-8");
		
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		if(!commentService.commentDelete(commentVo, authUser))
			return "redirect:/board/list";
		
		return "redirect:/board/view?no="+commentVo.getBoardNo()+"&pages="+pages+"&kwd="+kwd_encode;
	}
}
