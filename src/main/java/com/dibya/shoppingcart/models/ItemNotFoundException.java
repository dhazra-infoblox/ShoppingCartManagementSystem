package com.dibya.shoppingcart.models;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
	public ItemNotFoundException(String message) {
		super(message);
	}
}