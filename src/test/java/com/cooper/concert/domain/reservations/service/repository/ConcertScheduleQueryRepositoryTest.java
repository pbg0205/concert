package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.common.jpa"})
class ConcertScheduleQueryRepositoryTest {

	@Autowired
	private ConcertScheduleQueryRepository concertScheduleQueryRepository;

	@ParameterizedTest
	@MethodSource("concertScheduleSource")
	@DisplayName("콘서트 스케줄 조회 성공")
	@Sql("classpath:sql/concert_schedule_available_dates_repository_sample_data.sql")
	void 콘서트_스케줄_조회_성공(final Long concertId, final int offset, final int limit, final int expected) {
		// given, when
		final List<ConcertScheduleResult> sut = concertScheduleQueryRepository.findByAllByConcertIdAndPaging(
			concertId, offset, limit);

		// then
		assertThat(sut).hasSize(expected);
	}

	private static Stream<Arguments> concertScheduleSource() {
		return Stream.of(
			Arguments.of(1L, 0, 10, 10),
			Arguments.of(1L, 10, 10, 10),
			Arguments.of(1L, 20, 10, 5)
		);
	}

	@Test
	@DisplayName("콘서트 식별자를 통한 단일 콘서트 스케줄 조회 성공")
	@Sql("classpath:sql/concert_schedule_available_dates_repository_sample_data.sql")
	void 콘서트_식별자를_통한_단일_콘서트_스케줄_조회_성공() {
		// given
		final Long scheduleId = 6L; // 스케줄 날짜 : 2025-01-13T00:00

		// when
		final ConcertScheduleResult concertScheduleResult =
			concertScheduleQueryRepository.findConcertScheduleResultById(scheduleId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(concertScheduleResult.concertScheduleId()).isEqualTo(scheduleId);
			softAssertions.assertThat(concertScheduleResult.startDateTime())
				.isEqualTo(LocalDateTime.of(2025, 1, 13, 0, 0));
		});
	}
}
