package com.cooper.concert.domain.queues.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Getter
public class TokenNotFoundException extends TokenException {
	public TokenNotFoundException(final TokenErrorType errorType) {
		super(errorType);
	}
}
