package com.cafe24.mysite.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.service.SiteService;
import com.cafe24.mysite.vo.SiteVo;
import com.cafe24.security.Auth;

@Controller
@RequestMapping("/admin")
public class AdminMainController {
	
	@Autowired
	SiteService siteService;

	@Auth(role=Auth.Role.ADMIN)
	@RequestMapping({"/","/main"})
	public String main(
			Model model
			) {
		
		SiteVo siteVo = siteService.getOne();
		model.addAttribute("siteVo", siteVo);
		
		return "admin/main";
	}
	
	@Auth(role=Auth.Role.ADMIN)
	@RequestMapping(value="/main/update", method = RequestMethod.POST)
	public String site(
			@ModelAttribute @Valid SiteVo siteVo,
			BindingResult result,
			@RequestParam(value="file1") MultipartFile file1
			) {
		
		if(result.hasErrors()) return "redirect:/admin/";
		
		if(!siteService.mainUpdate(siteVo, file1)) return "redirect:/admin/";
		
		return "redirect:/admin/";
	}
}
