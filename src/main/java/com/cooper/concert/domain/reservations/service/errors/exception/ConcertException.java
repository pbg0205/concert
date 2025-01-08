package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;

@Getter
public abstract class ConcertException extends RuntimeException {
	private final ConcertErrorType errorType;

	public ConcertException(final ConcertErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
