package com.item.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


//Custom handler for access denied exceptions in JWT-secured endpoints.
//Returns a 403 Forbidden response with a JSON error message.
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
	//Handles AccessDeniedException by returning a 403 response with a custom message
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		System.out.println("Handling AccessDeniedException");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\":\"Access denied\"}");
		response.getWriter().flush();
	}
}
