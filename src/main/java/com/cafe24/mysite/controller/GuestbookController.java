package com.cafe24.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.service.GuestbookService;
import com.cafe24.mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	@Autowired
	GuestbookService guestbookService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(
			Model model
			) {
		
		List<GuestbookVo> list = guestbookService.getList();
		model.addAttribute("list", list);
		model.addAttribute("cnt", list.size());
		
		return "guestbook/list";
	}

	@RequestMapping(value="/list", method=RequestMethod.POST)
	public String list(
			GuestbookVo guestbookVo
			) {
		
		guestbookService.add(guestbookVo);
		
		return "redirect:/guestbook/list";
	}
	

	@RequestMapping(value="/deleteform", method=RequestMethod.GET)
	public String deleteform(
			@RequestParam(value="no", required=true, defaultValue="-1") int no,
			Model model
			) {
		
		model.addAttribute("no", no);
		
		return "guestbook/deleteform";
	}

	@RequestMapping(value="/deleteform", method=RequestMethod.POST)
	public String deleteform(
			@ModelAttribute GuestbookVo guestbookVo
			) {
		
		guestbookService.delete(guestbookVo);
		
		return "redirect:/guestbook/list";
	}
	
}
