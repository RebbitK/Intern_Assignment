package com.example.demo.global.exception;

import com.example.demo.global.common.CommonResponse;
import com.example.demo.global.exception.customException.DuplicateNicknameException;
import com.example.demo.global.exception.customException.DuplicateUsernameException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleBadRequestException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI(), ex);
		return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler({DuplicateNicknameException.class, DuplicateUsernameException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleConflictException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI(), ex);
		return createResponse(HttpStatus.CONFLICT, ex.getMessage());
	}


	private ResponseEntity<CommonResponse<ErrorResponse>> createResponse(HttpStatus status,
		String message) {
		ErrorResponse errorResponse = new ErrorResponse(status);
		return ResponseEntity.status(status)
			.body(CommonResponse.<ErrorResponse>builder()
				.data(errorResponse)
				.msg(message)
				.build());
	}
}