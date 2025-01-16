package com.cooper.concert.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.reservations.models.Reservation;

public interface ReservationTestRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findAllBySeatId(Long seatId);
}
