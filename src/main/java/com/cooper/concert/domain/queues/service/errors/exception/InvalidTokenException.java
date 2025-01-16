package com.cooper.concert.domain.queues.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Getter
public class InvalidTokenException extends TokenException {
	public InvalidTokenException(final TokenErrorType errorType) {
		super(errorType);
	}
}
