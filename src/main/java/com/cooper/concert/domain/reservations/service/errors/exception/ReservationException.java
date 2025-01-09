package com.cooper.concert.domain.reservations.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;

@Getter
public abstract class ReservationException extends RuntimeException {
	private final ReservationErrorType errorType;

	public ReservationException(final ReservationErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
