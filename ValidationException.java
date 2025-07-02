package com.item.exception;

//Custom exception thrown when validation rules are violated during item processing.
public class ValidationException extends RuntimeException {

	public ValidationException() {
		super();

	}

	public ValidationException(String message) {
		super(message);

	}

}
