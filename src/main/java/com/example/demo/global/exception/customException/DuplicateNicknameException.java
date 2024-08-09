package com.example.demo.global.exception.customException;

public class DuplicateNicknameException extends RuntimeException {
	public DuplicateNicknameException(String message) {
		super(message);
	}
}