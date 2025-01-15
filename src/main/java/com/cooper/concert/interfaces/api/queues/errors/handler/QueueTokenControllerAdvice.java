package com.cooper.concert.interfaces.api.queues.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenException;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class QueueTokenControllerAdvice {

	@ExceptionHandler(TokenException.class)
	public ResponseEntity<ApiResponse<?>> handleUserException(TokenException exception) {
		final TokenErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
