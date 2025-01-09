package com.cooper.concert.domain.reservations.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

	@Test
	@DisplayName("결제 대기 예약 생성 성공")
	void 결제_대기_예약_생성_성공() {
		// given
		final Long userId = 1L;
		final Long seatId = 1L;
		final UUID altId = UUID.fromString("019449c5-0b39-7680-9599-2ca3536facae"); // uuid ver7

		// when
		Reservation sut = Reservation.createPendingReservation(userId, seatId, altId);

		// then
		assertThat(sut.getStatus()).isEqualTo(ReservationStatus.PENDING);
	}

}
