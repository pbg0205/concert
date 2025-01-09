package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

import com.cooper.concert.domain.reservations.models.Reservation;

public interface ReservationCommandRepository {
	Reservation save(Reservation reservation);
	Reservation findById(Long id);
	List<Reservation> findByUserIdsAndReservationStatus(List<Long> userIds, String reservationStatus);
}
