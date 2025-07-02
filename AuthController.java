package com.item.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.item.dtos.AuthResponse;
import com.item.dto.LoginDto;
import com.item.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	//Service to handle authentication logic
	private AuthService authService;

	//Constructor injection for AuthService
	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	//Handles user login and returns a JWT token
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
		System.out.println(loginDto);

		var result = authService.login(loginDto);
		AuthResponse response = new AuthResponse();
		response.setJwtToken(result);
		return ResponseEntity.ok(response);
	}

}
