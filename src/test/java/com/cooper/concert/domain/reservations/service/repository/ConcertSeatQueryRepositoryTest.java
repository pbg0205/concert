package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class ConcertSeatQueryRepositoryTest {

	@Autowired
	private ConcertSeatQueryRepository concertSeatQueryRepository;

	@Test
	@DisplayName("좌석 상태와 콘서트 스케줄 식별자를 통해 좌석 조회 성공")
	@Sql("classpath:sql/repository/concert_seats_repository.sql")
	void 예약_가능_좌석_조회_성공() {
		// given
		final Long scheduleId = 1L; // 2025.01.30T19:00
		final String status = "AVAILABLE";

		// when
		final List<ConcertSeatResult> sut = concertSeatQueryRepository.findConcertSeatsByScheduleIdAndStatus(
			scheduleId, status);

		// then
		assertThat(sut).hasSize(25);
	}

	@Test
	@DisplayName("좌석 상태와 콘서트 스케줄 식별자를 통해 좌석 조회 성공")
	@Sql("classpath:sql/repository/concert_seats_repository.sql")
	void 예약_가능_좌석_가격_조회() {
		// given
		final Long seatId = 1L;

		// when
		final ConcertSeatPriceInfo sut = concertSeatQueryRepository.findConcertSeatPriceInfoById(seatId);

		// then
		assertThat(sut).extracting("price").isEqualTo(3000L);
	}

}
