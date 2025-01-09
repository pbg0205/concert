package com.cooper.concert.domain.reservations.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.reservations.models.Reservation;
import com.cooper.concert.domain.reservations.service.dto.response.ReservationCancelResult;
import com.cooper.concert.domain.reservations.service.repository.ReservationCommandRepository;

@ExtendWith(MockitoExtension.class)
class ReservationCancelServiceTest {

	@InjectMocks
	private ReservationCancelService reservationCancelService;

	@Mock
	private ReservationCommandRepository reservationCommandRepository;

	@Test
	@DisplayName("예약 대기 중에 만료된 토큰 사용자의 예약은 취소된다")
	void 예약_대기_중에_만료된_토큰_사용자의_예약_취소() {
		// given
		when(reservationCommandRepository.findByUserIdsAndReservationStatus(any(), any()))
			.thenReturn(List.of(
				Reservation.createPendingReservation(1L, 1L, UUID.randomUUID()),
				Reservation.createPendingReservation(2L, 2L, UUID.randomUUID()),
				Reservation.createPendingReservation(3L, 3L, UUID.randomUUID())));

		// when
		final List<ReservationCancelResult> sut
			= reservationCancelService.cancelReservations(List.of(1L, 2L, 3L));

		// then
		assertThat(sut).hasSize(3);
	}
}
