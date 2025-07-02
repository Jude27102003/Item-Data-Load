package com.item.exception;

//Custom exception thrown when an item with the specified ID is not found.
public class ItemNotFoundException extends RuntimeException {

	public ItemNotFoundException() {
		super();

	}

	public ItemNotFoundException(String message) {
		super(message);

	}

}
