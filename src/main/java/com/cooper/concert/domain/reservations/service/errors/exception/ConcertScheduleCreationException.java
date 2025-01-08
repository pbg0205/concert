package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;

@Getter
public class ConcertScheduleCreationException extends ConcertException {
	public ConcertScheduleCreationException(final ConcertErrorType errorType) {
		super(errorType);
	}
}
