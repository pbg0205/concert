package com.cooper.concert.interfaces.api.reservations.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertException;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class ConcertControllerAdvice {

	@ExceptionHandler(ConcertException.class)
	public ResponseEntity<ApiResponse<?>> handleUserException(ConcertException exception) {
		final ConcertErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
