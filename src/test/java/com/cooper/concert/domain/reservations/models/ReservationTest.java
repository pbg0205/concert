package com.cooper.concert.domain.reservations.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
		assertThat(sut).extracting("status").isEqualTo(ReservationStatus.PENDING);
	}

	@Test
	@DisplayName("예약 완료 성공")
	void 예약_완료_성공() {
		// given
		final Long userId = 1L;
		final Long seatId = 1L;
		final UUID altId = UUID.fromString("019449c5-0b39-7680-9599-2ca3536facae"); // uuid ver7

		Reservation sut = Reservation.createPendingReservation(userId, seatId, altId);

		// when
		final boolean completed = sut.complete();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(completed).isTrue();
			softAssertions.assertThat(sut).extracting("status").isEqualTo(ReservationStatus.RESERVED);
		});
	}

	@Test
	@DisplayName("예약 취소 성공")
	void 예약_취소_성공() {
		// given
		final Long userId = 1L;
		final Long seatId = 1L;
		final UUID altId = UUID.fromString("019449c5-0b39-7680-9599-2ca3536facae"); // uuid ver7

		Reservation sut = Reservation.createPendingReservation(userId, seatId, altId);

		// when
		final boolean canceled = sut.cancel();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(canceled).isTrue();
			softAssertions.assertThat(sut).extracting("status").isEqualTo(ReservationStatus.CANCELED);
		});
	}
}
