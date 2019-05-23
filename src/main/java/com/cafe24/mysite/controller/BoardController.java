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
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.BoardparamVo;
import com.cafe24.mysite.vo.CommentVo;
import com.cafe24.mysite.vo.UserVo;
import com.cafe24.security.Auth;
import com.cafe24.security.Auth.Role;
import com.cafe24.security.AuthUser;

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
	
	@Auth
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public String write(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));

		
		return "board/write";
	}
	
	@Auth
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@ModelAttribute BoardVo boardVo,
			@AuthUser UserVo authUser,
			@RequestParam(value="files1") MultipartFile files1
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		Long newNo = boardService.boardWrite(boardVo, authUser, files1);
		
		return "redirect:/board/view?no="+newNo+"&pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}
	

	@RequestMapping("/view")
	public String view(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@CookieValue(value="mysite_board_hit", required = true, defaultValue = "-1") String cookie,
			HttpServletResponse response,
			Model model
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		
		//이미 있으면 null을 반환
		cookie = boardService.updateHit(bpv.getNo(), cookie);
		if(cookie != null) {
			Cookie co = new Cookie("mysite_board_hit", cookie);
			co.setMaxAge(60*60*24);
			response.addCookie(co);
		}
		
		BoardVo boardVo = boardService.getOne(bpv.getNo());
		model.addAttribute("boardVo", boardVo);
		
		if(boardVo.getStatus() != 1) return "redirect:/";
		
		
		
		//댓글
		List<CommentVo> commentList = commentService.getList(bpv.getNo());
		model.addAttribute("commentList", commentList);
		
		return "board/view";
	}

	@Auth
	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public String modify(
			@ModelAttribute("bpv") BoardparamVo bpv,
			Model model,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		
		BoardVo boardVo = boardService.getOneModify(bpv.getNo());
		if(authUser.getNo() != boardVo.getUserNo()) return "redirect:/board/list";
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/modify";
	}
	
	@Auth
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@ModelAttribute BoardVo boardVo,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		
		BoardVo boardVoOld = boardService.getOne(boardVo.getNo());
		if(authUser.getNo() != boardVoOld.getUserNo()) return "redirect:/board/list";
		
		boardService.modify(boardVo);
		
		
		return "redirect:/board/view?no="+boardVo.getNo() + "&pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}
	
	@Auth
	@RequestMapping("/delete")
	public String delete(
			@ModelAttribute("bpv") BoardparamVo bpv,
			@AuthUser UserVo authUser
			) throws UnsupportedEncodingException {
		bpv.setKwd_decode(URLDecoder.decode(bpv.getKwd(), "utf-8"));
		bpv.setKwd_encode(URLEncoder.encode(bpv.getKwd_decode(), "utf-8"));
		
		 
		 
		 if(!boardService.delOne(bpv.getNo(), authUser)) return "redirect:/board/list";
		
		return "redirect:/board/list?pages="+bpv.getPages()+"&kwd="+bpv.getKwd_encode();
	}

}
