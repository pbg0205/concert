package com.cooper.concert.domain.reservations.service.errors;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.exception.ConcertException;

@Getter
public class ConcertNotFoundException extends ConcertException {
	public ConcertNotFoundException(final ConcertErrorType errorType) {
		super(errorType);
	}
}
