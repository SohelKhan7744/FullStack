package com.example.demo;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UserRepository userRepo;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		            String username = authentication.getName();
		
		           UserSelf user = userRepo.findByUsername(username).orElseThrow();
		           
		           user.setLastLogin(LocalDateTime.now());
		           
		           userRepo.save(user);
		           
		           response.setStatus(HttpServletResponse.SC_OK);
	}
	
}
