package com.item.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data Transfer Object for sending authentication response.
//Contains the JWT token and its type
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

	private String jwtToken;

	private String type = "Bearer";

}