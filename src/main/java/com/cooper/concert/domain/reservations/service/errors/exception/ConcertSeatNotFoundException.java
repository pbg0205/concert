package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;

@Getter
public class ConcertSeatNotFoundException extends ConcertException {
	public ConcertSeatNotFoundException(final ConcertErrorType errorType) {
		super(errorType);
	}
}
