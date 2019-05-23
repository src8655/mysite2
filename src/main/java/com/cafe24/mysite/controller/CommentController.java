package com.cafe24.mysite.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.vo.BoardparamVo;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;
import com.cafe24.security.AuthUser;

@Controller
@RequestMapping("/board/comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@Valid
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@ModelAttribute @Valid CommentVo commentVo,
			BindingResult result,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		if(result.hasErrors()) return "redirect:/";
		
		commentService.commentWrite(commentVo, authUser);
		
		return "redirect:/board/view?no="+commentVo.getBoardNo()+"&pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}
	
	@Valid
	@RequestMapping("/delete")
	public String delete(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@ModelAttribute CommentVo commentVo,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));

		if(!commentService.commentDelete(commentVo, authUser))
			return "redirect:/";
		
		return "redirect:/board/view?no="+commentVo.getBoardNo()+"&pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}
}
