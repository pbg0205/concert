package com.cooper.concert.interfaces.api.payments.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentException;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class PaymentControllerAdvice {

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ApiResponse<?>> handleUserException(PaymentException exception) {
		final PaymentErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
