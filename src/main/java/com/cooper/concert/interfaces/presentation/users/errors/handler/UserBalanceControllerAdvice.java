package com.cooper.concert.interfaces.presentation.users.errors.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.business.errors.UserErrorType;
import com.cooper.concert.business.errors.exception.UserException;
import com.cooper.concert.common.api.support.response.ApiResponse;

@RestControllerAdvice
public class UserBalanceControllerAdvice {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse<?>> handleUserException(UserException exception) {
		final UserErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(),
				errorType.getMessage()
			));
	}
}
