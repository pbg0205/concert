package com.cooper.concert.domain.queues.service.errors.exception;

import java.io.Serial;

import lombok.Getter;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;

@Getter
public class ExpiredQueueTokenException extends TokenException {

	@Serial
	private static final long serialVersionUID = 3755752709389801679L;

	public ExpiredQueueTokenException(final TokenErrorType errorType) {
		super(errorType);
	}
}
