package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;

@Getter
public class ReservationCanceledException extends ReservationException {
	public ReservationCanceledException(final ReservationErrorType errorType) {
		super(errorType);
	}
}
