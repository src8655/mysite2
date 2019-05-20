package com.cafe24.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
			Model model
			) {
		
		List<BoardVo> list = boardService.getList();
		model.addAttribute("list", list);
		model.addAttribute("cnt", list.size());
		
		return "board/list";
	}
	
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public String write(
			@RequestParam(value="groupNo", required = true, defaultValue = "-1") int groupNo,
			@RequestParam(value="orderNo", required = true, defaultValue = "-1") int orderNo,
			@RequestParam(value="depth", required = true, defaultValue = "-1") int depth,
			Model model,
			HttpSession session
			) {
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";

		model.addAttribute("groupNo", groupNo);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("depth", depth);
		
		return "board/write";
	}
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute BoardVo boardVo,
			HttpSession session
			) {
		if(session == null) return "redirect:/board/list";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/board/list";
		
		boardService.boardWrite(boardVo, authUser);
		
		return "redirect:/board/list";
	}
	

	@RequestMapping("/view")
	public String view(
			@RequestParam(value="no", required = true, defaultValue = "-1") Long no,
			Model model
			) {
		
		BoardVo boardVo = boardService.getOne(no);
		model.addAttribute("boardVo", boardVo);
		
		return "board/view";
	}
	

}
