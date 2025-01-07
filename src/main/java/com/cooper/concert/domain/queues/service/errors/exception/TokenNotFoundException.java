package com.cooper.concert.domain.queues.service.errors.exception;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

public class TokenNotFoundException extends TokenException {
	public TokenNotFoundException(final TokenErrorType errorType) {
		super(errorType);
	}
}
