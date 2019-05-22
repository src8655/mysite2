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
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(
			@RequestParam(value="email", required=true, defaultValue="") String email,
			@RequestParam(value="password", required=true, defaultValue="") String password,
			Model model,
			HttpSession session
			) {
		
		UserVo authUser = userService.getUser(new UserVo(email, password));
		if(authUser == null) {
			model.addAttribute("result", false);
			return "user/login";
		}
		
		session.setAttribute("authUser", authUser);
		
		return "redirect:/";
	}
	@RequestMapping("/logout")
	public String logout(
			HttpSession session
			) {
		session.setAttribute("authUser", null);
		
		return "redirect:/";
	}
	

	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(
			Model model,
			HttpSession session
			) {
		if(session == null) return "redirect:/";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/";
		
		UserVo userVo = userService.getUser(authUser.getNo());
		
		model.addAttribute("userVo", userVo);
		
		return "user/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(
			UserVo userVo,
			HttpSession session
			) {
		if(session == null) return "redirect:/";
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) return "redirect:/";
		
		userService.update(userVo, authUser);
		
		authUser.setName(userVo.getName());
		session.setAttribute("authUser", authUser);
		
		return "redirect:/";
	}
	
	


}
