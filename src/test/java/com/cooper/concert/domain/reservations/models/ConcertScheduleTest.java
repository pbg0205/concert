package com.cooper.concert.domain.reservations.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleCreationException;

class ConcertScheduleTest {

	@Test
	@DisplayName("종료 시간이 시작 시간보다 앞서면 생성 실패")
	void 종료_시간이_시작_시간보다_앞서면_생성_실패() {
		// given
		final Long concertId = 1L;
		final LocalDateTime startAt = LocalDateTime.of(2025, 1, 10, 20, 0);
		final LocalDateTime endAt = LocalDateTime.of(2025, 1, 10, 17, 0);

		// when, then
		assertThatThrownBy(() -> ConcertSchedule.create(concertId, startAt, endAt))
			.isInstanceOf(ConcertScheduleCreationException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((ConcertErrorType)errorType)).isEqualTo(
				ConcertErrorType.INVALID_CONCERT_SCHEDULE));
	}

	@Test
	@DisplayName("시작 시간이 종료 시간이 보다 앞서면 생성 성공")
	void 시작_시간이_종료_시간이_보다_앞서면_생성_성공() {
		// given
		final Long concertId = 1L;
		final LocalDateTime startAt = LocalDateTime.of(2025, 1, 10, 18, 0);
		final LocalDateTime endAt = LocalDateTime.of(2025, 1, 10, 20, 0);

		// when
		final ConcertSchedule sut = ConcertSchedule.create(concertId, startAt, endAt);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).extracting("concertId").isEqualTo(concertId);
			softAssertions.assertThat(sut).extracting("startAt").isEqualTo(startAt);
			softAssertions.assertThat(sut).extracting("endAt").isEqualTo(endAt);
		});
	}
}
