package com.cooper.concert.api.components.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.api.support.error.CommonErrorType;
import com.cooper.concert.api.support.response.ApiResponse;

@Slf4j
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
		final CommonErrorType errorType = CommonErrorType.ERROR_UNKNOWN;

		log.error("exception : {}, message : {}\n stackTraces: {}",
			exception.getClass().getSimpleName(),
			exception.getMessage(),
			String.join("\n", getStackTraces(exception)));

		return ResponseEntity.internalServerError()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException exception) {
		final CommonErrorType errorType = CommonErrorType.ERROR_UNKNOWN;

		logWarn(exception);

		return ResponseEntity.internalServerError()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(
		HttpMessageNotReadableException exception) {

		logWarn(exception);

		final CommonErrorType errorType = CommonErrorType.ERROR_MESSAGE_UNREADABLE;
		return ResponseEntity.badRequest()
			.body(ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	private void logWarn(final RuntimeException exception) {
		log.warn("exception : {}, message : {}\n stackTraces: {}",
			exception.getClass().getSimpleName(),
			exception.getMessage(),
			String.join("\n", getStackTraces(exception)));
	}

	private List<String> getStackTraces(final Exception exception) {
		return Arrays.stream(exception.getStackTrace())
			.map(StackTraceElement::toString)
			.toList();
	}

}
