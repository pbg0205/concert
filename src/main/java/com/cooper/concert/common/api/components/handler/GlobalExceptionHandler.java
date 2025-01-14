package com.cooper.concert.common.api.components.handler;

import java.util.Arrays;
import java.util.List;

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
		logError(exception);

		final CommonErrorType errorType = CommonErrorType.ERROR_UNKNOWN;
		return ResponseEntity.internalServerError()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(
		HttpMessageNotReadableException exception) {
		logError(exception);

		final CommonErrorType errorType = CommonErrorType.ERROR_MESSAGE_UNREADABLE;
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	private void logError(final RuntimeException exception) {
		final List<String> stackTraces = Arrays.stream(exception.getStackTrace())
			.map(StackTraceElement::toString)
			.toList();

		log.error("exception : {}, message : {}\n stackTraces: {}",
			exception.getClass().getSimpleName(),
			exception.getMessage(),
			String.join("\n", stackTraces));
	}
}
