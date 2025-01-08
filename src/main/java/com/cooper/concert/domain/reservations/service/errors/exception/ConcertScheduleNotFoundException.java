package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;

@Getter
public class ConcertScheduleNotFoundException extends ConcertException {
	public ConcertScheduleNotFoundException(final ConcertErrorType errorType) {
		super(errorType);
	}
}
