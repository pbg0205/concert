package com.cooper.concert.common.api.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
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
}
