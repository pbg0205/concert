package com.cooper.concert.domain.reservations.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorType {
	CONCERT_SEAT_OCCUPIED(ReservationErrorCode.ERROR_RESERVATION01, "해당 좌석은 점유되어 있습니다"),
	RESERVATION_CANCELED(ReservationErrorCode.ERROR_RESERVATION02, "해당 예약은 취소 되었습니다."),
	RESERVATION_RESERVED(ReservationErrorCode.ERROR_RESERVATION03, "해당 예약은 이미 예약 되었습니다.");

	private final ReservationErrorCode errorCode;
	private final String message;
}
