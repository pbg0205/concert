package com.cooper.concert.interfaces.api.reservations.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationException;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class ReservationControllerAdvice {

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<ApiResponse<?>> handleUserException(ReservationException exception) {
		final ReservationErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
