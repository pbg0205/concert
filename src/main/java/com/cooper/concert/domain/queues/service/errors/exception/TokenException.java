package com.cooper.concert.domain.queues.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Getter
public abstract class TokenException extends RuntimeException {
	private final TokenErrorType errorType;

	public TokenException(final TokenErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
