package com.cooper.concert.common.api.components.interceptor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Interceptor;
import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.domain.queues.service.QueueTokenValidationService;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Interceptor
@RequiredArgsConstructor
public class QueueTokenValidationInterceptor implements HandlerInterceptor {

	private static final String QUEUE_TOKEN_HEADER_NAME = "QUEUE-TOKEN";

	private final QueueTokenValidationService queueTokenValidationService;
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
		throws Exception {

		final UUID tokenId = UUID.fromString(request.getHeader(QUEUE_TOKEN_HEADER_NAME));
		final boolean tokenProcessingValid =
			queueTokenValidationService.validateQueueTokenProcessing(tokenId, LocalDateTime.now());

		if (!tokenProcessingValid) {
			final TokenErrorType errorType = TokenErrorType.PROCESSING_TOKEN_INVALID;
			final ApiResponse<?> errorResponse = ApiResponse.error(errorType.getErrorCode().name(), errorType.getMessage());

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
			return false;
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
