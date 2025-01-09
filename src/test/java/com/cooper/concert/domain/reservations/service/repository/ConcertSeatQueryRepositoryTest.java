package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.reservations.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class ConcertSeatQueryRepositoryTest {

	@Autowired
	private ConcertSeatQueryRepository concertSeatQueryRepository;

	@Test
	@DisplayName("좌석 상태와 콘서트 스케줄 식별자를 통해 좌석 조회 성공")
	@Sql("classpath:sql/concert_seats_repository.sql")
	void 예약_가능_좌석_조회_성공() {
		// given
		final Long scheduleId = 1L; // 2025.01.30T19:00
		final String status = "AVAILABLE";
		final Integer limit = 20;
		final Integer offset = 0;

		// when
		final List<ConcertSeatResult> sut = concertSeatQueryRepository.findConcertSeatsByScheduleIdAndStatusAndPaging(
			scheduleId, status, offset, limit);

		// then
		assertThat(sut).hasSize(20);
	}

	@Test
	@DisplayName("예약 가능 좌석 조회 페이지네이션 성공")
	@Sql("classpath:sql/concert_seats_repository.sql")
	void 예약_가능_좌석_조회_페이지네이션_성공() {
		// given
		final Long scheduleId = 1L;
		final String status = "AVAILABLE";
		final Integer limit = 20;
		final Integer offset = 20;

		// when
		final List<ConcertSeatResult> sut = concertSeatQueryRepository.findConcertSeatsByScheduleIdAndStatusAndPaging(
			scheduleId, status, offset, limit);

		// then
		assertThat(sut).hasSize(5);
	}

	@Test
	@DisplayName("좌석 상태와 콘서트 스케줄 식별자를 통해 좌석 조회 성공")
	@Sql("classpath:sql/concert_seats_repository.sql")
	void 예약_가능_좌석_가격_조회() {
		// given
		final Long seatId = 1L;

		// when
		final ConcertSeatPriceInfo sut = concertSeatQueryRepository.findConcertSeatPriceInfoById(seatId);

		// then
		assertThat(sut).extracting("price").isEqualTo(3000L);
	}

}
