package com.cooper.concert.domain.queues.service.errors.exception;

import java.io.Serial;

import lombok.Getter;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Getter
public class InvalidQueueTokenException extends TokenException {

	@Serial
	private static final long serialVersionUID = 4743392853290125930L;

	public InvalidQueueTokenException(final TokenErrorType errorType) {
		super(errorType);
	}
}
