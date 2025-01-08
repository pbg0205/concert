package com.cooper.concert.domain.reservations.service.errors.exception;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;

public class ConcertCreationException extends ConcertException {
	public ConcertCreationException(final ConcertErrorType errorType) {
		super(errorType);
	}
}
