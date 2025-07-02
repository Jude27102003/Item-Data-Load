package com.item.service;

import com.item.dto.LoginDto;

//Service interface for handling authentication operations.
public interface AuthService {

	//Authenticates the user and returns a JWT token as a String if credentials are valid.
	String login(LoginDto loginDto);

}
