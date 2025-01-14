package com.cooper.concert.common.api.components.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.common.api.support.error.CommonErrorType;
import com.cooper.concert.common.api.support.response.ApiResponse;

@Slf4j
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException exception) {
		log.error("exception : {}, message : {}", exception.getClass().getSimpleName(), exception.getMessage());

		final CommonErrorType errorType = CommonErrorType.ERROR_UNKNOWN;
		return ResponseEntity.internalServerError()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		log.error("exception : {}, message : {}", exception.getClass().getSimpleName(), exception.getMessage());

		final CommonErrorType errorType = CommonErrorType.ERROR_MESSAGE_UNREADABLE;
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
