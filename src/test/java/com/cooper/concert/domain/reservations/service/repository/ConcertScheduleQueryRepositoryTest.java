package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.reservations.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class ConcertScheduleQueryRepositoryTest {

	@Autowired
	private ConcertScheduleQueryRepository concertScheduleQueryRepository;

	@ParameterizedTest
	@MethodSource("concertScheduleSource")
	@DisplayName("콘서트 스케줄 조회 성공")
	@Sql("classpath:sql/concert_schedules.sql")
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
}
