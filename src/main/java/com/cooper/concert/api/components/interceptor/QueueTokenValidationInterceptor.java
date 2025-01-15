package com.cooper.concert.api.components.interceptor;

import java.time.LocalDateTime;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.components.dto.TokenHeaderData;
import com.cooper.concert.api.support.annotations.Interceptor;
import com.cooper.concert.domain.queues.service.QueueTokenValidationService;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.InvalidTokenException;

@Interceptor
@RequiredArgsConstructor
public class QueueTokenValidationInterceptor implements HandlerInterceptor {

	private static final String QUEUE_TOKEN_HEADER_NAME = "QUEUE-TOKEN";

	private final QueueTokenValidationService queueTokenValidationService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
		throws Exception {

		final TokenHeaderData tokenHeaderData = new TokenHeaderData(request.getHeader(QUEUE_TOKEN_HEADER_NAME));

		final boolean tokenProcessingValid =
			queueTokenValidationService.validateQueueTokenProcessing(tokenHeaderData.getTokenId(), LocalDateTime.now());

		if (!tokenProcessingValid) {
			throw new InvalidTokenException(TokenErrorType.PROCESSING_TOKEN_INVALID);
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
