package com.cooper.concert.domain.reservations.service.repository;

import com.cooper.concert.domain.reservations.models.ConcertSeat;

public interface ConcertSeatCommandRepository {
	ConcertSeat findById(Long id);
}
