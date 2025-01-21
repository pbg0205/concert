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
import com.cooper.concert.storage.redis.redisson.components.annotations.DistributedLock;

@Service
@RequiredArgsConstructor
public class ConcertReservationService {

	private final ReservationAltIdGenerator reservationAltIdGenerator;
	private final ConcertSeatCommandRepository concertSeatCommandRepository;
	private final ReservationCommandRepository reservationCommandRepository;

	@DistributedLock(key = "'SEAT:' + #seatId")
	public ConcertReservationInfo reserveSeat(final Long userId, final Long seatId) {
		final ConcertSeat concertSeat = Optional.ofNullable(concertSeatCommandRepository.findByIdForUpdate(seatId))
			.orElseThrow(() -> new ConcertSeatNotFoundException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));

		if (concertSeat.isUnavailable()) {
			throw new ReservationUnavailableException(ReservationErrorType.CONCERT_SEAT_OCCUPIED);
		}

		concertSeat.updateUnavailable();

		final UUID reservationAltId = reservationAltIdGenerator.generateAltId();
		final Reservation savedReservation = reservationCommandRepository.save(
			Reservation.createPendingReservation(userId, concertSeat.getId(), reservationAltId));

		return new ConcertReservationInfo(savedReservation.getId(), savedReservation.getAltId());
	}

	@Transactional
	public ConcertReservationCompletedInfo completeReservation(final Long reservationId) {
		final Reservation reservation = reservationCommandRepository.findById(reservationId);

		reservation.complete();

		return new ConcertReservationCompletedInfo(
			reservation.getId(), reservation.getUserId(), reservation.getSeatId(), reservation.getAltId());
	}
}
