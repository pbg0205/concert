package com.cooper.concert.domain.reservations.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorType {
	CONCERT_SEAT_OCCUPIED(ReservationErrorCode.ERROR_RESERVATION01, "해당 좌석은 점유되어 있습니다");

	private final ReservationErrorCode errorCode;
	private final String message;
}
