package com.cooper.concert.domain.reservations.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertReservationService {

	private final ReservationAltIdGenerator reservationAltIdGenerator;
	private final ConcertSeatCommandRepository concertSeatCommandRepository;
	private final ReservationCommandRepository reservationCommandRepository;

	public ConcertReservationInfo reserveSeat(final Long userId, final Long seatId) {
		final ConcertSeat concertSeat = Optional.ofNullable(concertSeatCommandRepository.findById(seatId))
			.orElseThrow(() -> new ConcertSeatNotFoundException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));

		if (concertSeat.isUnavailable()) {
			throw new ReservationUnavailableException(ReservationErrorType.CONCERT_SEAT_OCCUPIED);
		}

		concertSeat.updateUnavailable();

		final UUID reservationAltId = reservationAltIdGenerator.generateAltId();
		final Reservation savedReservation = reservationCommandRepository.save(
			Reservation.createPendingReservation(concertSeat.getId(), userId, reservationAltId));

		return new ConcertReservationInfo(savedReservation.getId(), savedReservation.getAltId());
	}
}
