package com.cooper.concert.interfaces.api.users.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserException;
import com.cooper.concert.common.api.support.response.ApiResponse;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
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
