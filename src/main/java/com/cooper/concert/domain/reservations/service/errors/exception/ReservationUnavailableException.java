package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;

@Getter
public class ReservationUnavailableException extends ReservationException {
	public ReservationUnavailableException(final ReservationErrorType errorType) {
		super(errorType);
	}
}
