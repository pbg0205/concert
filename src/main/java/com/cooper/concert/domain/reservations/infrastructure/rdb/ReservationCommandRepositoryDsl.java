package com.cooper.concert.domain.reservations.infrastructure.rdb;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.Reservation;
import com.cooper.concert.domain.reservations.service.repository.ReservationCommandRepository;

@Repository
@RequiredArgsConstructor
public class ReservationCommandRepositoryDsl implements ReservationCommandRepository {

	private final JpaReservationRepository reservationRepository;

	@Override
	public Reservation save(final Reservation reservation) {
		return reservationRepository.save(reservation);
	}
}
