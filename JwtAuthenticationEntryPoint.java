package com.item.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Custom entry point for handling unauthorized access attempts in JWT authentication.
//Returns a 401 Unauthorized response with a JSON error message.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	// Handles authentication failures by returning a 401 response with the
	// exception message
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,

			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\":" + authException.getMessage() + "}");
	}

}