package com.cafe24.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.cafe24.mysite.service.UserService;
import com.cafe24.mysite.vo.UserVo;
import com.cafe24.security.Auth;
import com.cafe24.security.AuthUser;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "user/join";
	}

	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(
			@ModelAttribute @Valid UserVo userVo,
			BindingResult result,
			Model model
			) {
		
		if(result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			
			return "user/join";
		}
		
		userService.join(userVo);
		
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping("/joinsuccess")
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@Auth
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(
			Model model,
			@AuthUser UserVo authUser
			) {
		
		UserVo userVo = userService.getUser(authUser.getNo());
		
		model.addAttribute("userVo", userVo);
		
		return "user/update";
	}
	
	@Auth
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(
			UserVo userVo,
			@AuthUser UserVo authUser,
			HttpSession session
			) {
		
		userService.update(userVo, authUser);
		
		authUser.setName(userVo.getName());
		session.setAttribute("authUser", authUser);
		
		return "redirect:/";
	}
	
	


}
