package com.item.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data Transfer Object for sending error messages in API responses.
//Typically used in exception handling to return meaningful error details.
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {

	private String message;

}
