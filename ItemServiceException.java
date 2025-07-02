package com.item.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Custom exception used to represent service-level errors in item operations.
//Includes an HTTP status code and a descriptive error message.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ItemServiceException extends RuntimeException {

	private HttpStatus status;

	private String message;

}