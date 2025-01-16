package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.reservations.models.Reservation;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class ReservationCommandRepositoryTest {

	@Autowired
	private ReservationCommandRepository reservationCommandRepository;

	@Test
	@DisplayName("예약 저장 성공")
	void 예약_저장_성공() {
		// given
		final UUID altId = UUID.fromString("019449f7-8f1c-78a8-8afa-40c273e4708a");
		final Reservation reservation = Reservation.createPendingReservation(1L, 1L, altId);

		// when
		final Reservation sut = reservationCommandRepository.save(reservation);

		// then
		assertThat(sut.getId()).isEqualTo(reservation.getId());
	}

	@Test
	@DisplayName("대체키 기반 예약 조회 성공")
	@Sql("classpath:sql/reservation_repository.sql")
	void 대체키_기반_예약_조회_성공() {
		// given
		final Long reservationId = 1L;

		// when
		final Reservation sut = reservationCommandRepository.findById(reservationId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getId()).isNotNull();
			softAssertions.assertThat(sut.getId()).isEqualTo(reservationId);
		});
	}

	@Test
	@DisplayName("사용자 예약 취소 성공")
	@Sql("classpath:sql/reservation_cancel_repository.sql")
	void 사용자_예약_취소_성공() {
		// given
		final List<Long> userIds = List.of(1L, 2L, 3L, 4L);
		String status = "PENDING";

		// when
		final List<Reservation> sut = reservationCommandRepository.findByUserIdsAndReservationStatus(userIds, status);

		// then
		assertThat(sut).hasSize(4);
	}

}
