package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.reservations.models.ConcertSeat;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class ConcertSeatCommandRepositoryTest {

	@Autowired
	private ConcertSeatCommandRepository concertSeatCommandRepository;

	@Test
	@DisplayName("변경을 위한 콘서트 좌석 조회")
	@Sql("classpath:sql/concert_seat_repository.sql")
	void 변경을_위한_콘서트_좌석_조회() {
		// given
		final Long id = 1L;

		// when
		final ConcertSeat sut = concertSeatCommandRepository.findById(id);

		// then
		assertThat(sut.getId()).isEqualTo(id);
	}

	@Test
	@DisplayName("점유 취소를 위한 콘서트 좌석 조회")
	@Sql("classpath:sql/concert_seats_cancel_repository.sql")
	void 점유_취소를_위한_콘서트_좌석_조회() {
		// given
		List<Long> seatIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);
		String seatStatus = "UNAVAILABLE";

		// when
		final List<ConcertSeat> sut =
			concertSeatCommandRepository.findAllByIdsAndSeatStatus(seatIds, seatStatus);

		// then
		assertThat(sut).hasSize(4);
	}
}
