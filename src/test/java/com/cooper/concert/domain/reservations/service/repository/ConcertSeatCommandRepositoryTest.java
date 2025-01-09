package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.reservations.models.ConcertSeat;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
}