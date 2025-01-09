package com.cooper.concert.domain.reservations.infrastructure.rdb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.reservations.models.Reservation;

public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
}
