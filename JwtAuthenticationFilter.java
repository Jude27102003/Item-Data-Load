package com.item.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.item.exception.ItemServiceException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//authenticates requests using JWT tokens.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;
	
	private UserDetailsService userDetailsService;

	//Constructor based injection
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		super();
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	//Filters each request to validate JWT and set authentication context.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String token = getTokenFromRequest(request);
			if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

				String username = jwtTokenProvider.getUsername(token);
				System.out.println(username + "====================");
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println(token);
				System.out.println(username);
				System.out.println(userDetails.getAuthorities());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				System.out.println(userDetails.getAuthorities() + "=============");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (ItemServiceException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\":" + e.getMessage() + "}");
			return;
		}

		filterChain.doFilter(request, response);

	}

	//Extracts JWT token from the Authorization header.
	private String getTokenFromRequest(HttpServletRequest request) {

		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {

			String token = bearerToken.substring(7);
			return token;
		}

		return null;
	}

}
