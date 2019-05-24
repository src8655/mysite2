package com.cafe24.security;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.vo.UserVo;

public class CookieIntercepter extends HandlerInterceptorAdapter {
	
	@Autowired
	BoardService boardService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Long no = -1L;
		String no_str = request.getParameter("no");
		if(no_str != null) no = Long.parseLong(no_str);
		
		Cookie[] cookies = request.getCookies();
		
		String cookie = "-1";
		for(Cookie c : cookies) {
			if(c.getName().equals("mysite_board_hit")) {
				cookie = c.getValue();
				break;
			}
		}
		
		//이미 있으면 null을 반환
		cookie = boardService.updateHit(no, cookie);
		if(cookie != null) {
			Cookie co = new Cookie("mysite_board_hit", cookie);
			co.setMaxAge(60*60*24);
			response.addCookie(co);
		}
		
		return true;
	}



}
