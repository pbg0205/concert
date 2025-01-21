package com.cooper.concert.domain.reservations.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.reservations.models.ConcertSeat;
import com.cooper.concert.domain.reservations.models.Reservation;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationCompletedInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationInfo;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertSeatNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationUnavailableException;
import com.cooper.concert.domain.reservations.service.generator.ReservationAltIdGenerator;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatCommandRepository;
import com.cooper.concert.domain.reservations.service.repository.ReservationCommandRepository;

@ExtendWith(MockitoExtension.class)
class ConcertReservationServiceTest {

	private ConcertReservationService concertReservationService;

	private ReservationAltIdGenerator reservationAltIdGenerator;

	private ConcertSeatCommandRepository concertSeatCommandRepository;

	private ReservationCommandRepository reservationCommandRepository;

	@BeforeEach
	void setUp() {
		this.reservationAltIdGenerator = new ReservationAltIdGenerator();
		this.concertSeatCommandRepository = Mockito.mock(ConcertSeatCommandRepository.class);
		this.reservationCommandRepository = Mockito.mock(ReservationCommandRepository.class);
		this.concertReservationService = new ConcertReservationService(reservationAltIdGenerator,
			concertSeatCommandRepository, reservationCommandRepository);
	}

	@Test
	@DisplayName("콘서트 좌석 조회를 실패하면 ConcertSeatNotFoundException 반환")
	void 콘서트_좌석_조회를_실패하면_ReservationUnavailableException_반환() {
		// given
		when(concertSeatCommandRepository.findByIdWithOptimisticLock(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> concertReservationService.reserveSeat(1L, 1L))
			.isInstanceOf(ConcertSeatNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat((ConcertErrorType)errorType)
				.isEqualTo(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));
	}

	@Test
	@DisplayName("콘서트 좌석 점유 실패하면 ReservationUnavailableException 반환")
	void 콘서트_좌석_점유_실패하면_ReservationUnavailableException_반환() {
		// given
		final ConcertSeat seat = ConcertSeat.createAvailableSeat(1L, 1L);
		seat.updateUnavailable();

		when(concertSeatCommandRepository.findByIdWithOptimisticLock(any())).thenReturn(seat);

		// when, then
		assertThatThrownBy(() -> concertReservationService.reserveSeat(1L, 1L))
			.isInstanceOf(ReservationUnavailableException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat((ReservationErrorType)errorType)
				.isEqualTo(ReservationErrorType.CONCERT_SEAT_OCCUPIED));
	}

	@Test
	@DisplayName("콘서트 좌석 점유 성공하면 예약 성공")
	void 콘서트_좌석_점유_성공하면_예약_성공() {
		// given
		final ConcertSeat concertSeat = ConcertSeat.createAvailableSeat(1L, 1L);
		final UUID reservationAltId = reservationAltIdGenerator.generateAltId();
		final Long userId = 1L;
		final Long seatId = 1L;

		when(concertSeatCommandRepository.findByIdWithOptimisticLock(any())).thenReturn(concertSeat);
		when(reservationCommandRepository.save(any()))
			.thenReturn(Reservation.createPendingReservation(userId, seatId, reservationAltId));

		// when
		final ConcertReservationInfo sut = concertReservationService.reserveSeat(userId, seatId);

		// then
		assertThat(sut.reservationAltId()).isEqualTo(reservationAltId);
	}

	@Test
	@DisplayName("콘서트 예약을 성공하면 유저와 좌석 아이디 반환")
	void 콘서트_예약을_성공하면_유저와_좌석_아이디_반환() {
		// given
		final Long reservationId = 1L;
		final UUID reservationAltId = reservationAltIdGenerator.generateAltId();
		final Long userId = 1L;
		final Long seatId = 1L;

		when(reservationCommandRepository.findById(any()))
			.thenReturn(Reservation.createPendingReservation(userId, seatId, reservationAltId));

		// when
		final ConcertReservationCompletedInfo sut =
			concertReservationService.completeReservation(reservationId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.userId()).isEqualTo(userId);
			softAssertions.assertThat(sut.seatId()).isEqualTo(seatId);
		});
	}
}
