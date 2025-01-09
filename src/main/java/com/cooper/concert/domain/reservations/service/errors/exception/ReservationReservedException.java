package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;

@Getter
public class ReservationReservedException extends ReservationException {
	public ReservationReservedException(final ReservationErrorType errorType) {
		super(errorType);
	}
}
