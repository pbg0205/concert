package com.cooper.concert.domain.reservations.service.repository;

import com.cooper.concert.domain.reservations.models.Reservation;

public interface ReservationCommandRepository {
	Reservation save(Reservation reservation);
	Reservation findById(Long id);
}
