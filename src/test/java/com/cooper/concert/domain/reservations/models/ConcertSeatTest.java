package com.cooper.concert.domain.reservations.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConcertSeatTest {

	@Test
	@DisplayName("콘서트 예약 가능 좌석 생성 성공")
	void 콘서트_예약_가능_좌석_생성_성공() {
		// given
		final Long scheduleId = 1L;
		final Long seatNumber = 1L;

		// when
		final ConcertSeat sut = ConcertSeat.createAvailableSeat(scheduleId, seatNumber);

		// then
		assertThat(sut).extracting("status").isEqualTo(ConcertSeatStatus.AVAILABLE);
	}

	@Test
	@DisplayName("콘서트 좌석 예약 불가능 상태 변경")
	void 콘서트_좌석_예약_불가능_상태_변경() {
		// given
		final Long scheduleId = 1L;
		final Long seatNumber = 1L;

		final ConcertSeat sut = ConcertSeat.createAvailableSeat(scheduleId, seatNumber);

		// when
		final boolean updated = sut.updateUnavailable();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(updated).isTrue();
			softAssertions.assertThat(sut).extracting("status")
				.isEqualTo(ConcertSeatStatus.UNAVAILABLE);
		});
	}

	@Test
	@DisplayName("콘서트 좌석 예약 가능 상태 변경")
	void 콘서트_좌석_예약_가능_상태_변경() {
		// given
		final Long scheduleId = 1L;
		final Long seatNumber = 1L;

		final ConcertSeat sut = ConcertSeat.createAvailableSeat(scheduleId, seatNumber);
		sut.updateUnavailable();

		// when
		final boolean updated = sut.updateAvailable();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(updated).isTrue();
			softAssertions.assertThat(sut).extracting("status").isEqualTo(ConcertSeatStatus.AVAILABLE);
		});
	}

	@Test
	@DisplayName("콘서트 좌석 예약 불가능 상태 확인")
	void 콘서트_좌석_예약_불가능_상태_확인() {
		// given
		final Long scheduleId = 1L;
		final Long seatNumber = 1L;

		final ConcertSeat sut = ConcertSeat.createAvailableSeat(scheduleId, seatNumber);
		sut.updateUnavailable();

		// when
		final boolean updated = sut.updateAvailable();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(updated).isTrue();
			softAssertions.assertThat(sut).extracting("status").isEqualTo(ConcertSeatStatus.AVAILABLE);
		});
	}
}
