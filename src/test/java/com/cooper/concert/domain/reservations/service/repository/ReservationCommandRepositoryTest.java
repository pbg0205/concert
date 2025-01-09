package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.reservations.models.Reservation;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class ReservationCommandRepositoryTest {

	@Autowired
	private ReservationCommandRepository reservationCommandRepository;

	@Test
	@DisplayName("예약 저장 성공")
	void Î예약_저장_성공() {
		// given
		final UUID altId = UUID.fromString("019449f7-8f1c-78a8-8afa-40c273e4708a");
		final Reservation reservation = Reservation.createPendingReservation(1L, 1L, altId);

		// when
		final Reservation sut = reservationCommandRepository.save(reservation);

		// then
		assertThat(sut.getId()).isEqualTo(reservation.getId());
	}
}
