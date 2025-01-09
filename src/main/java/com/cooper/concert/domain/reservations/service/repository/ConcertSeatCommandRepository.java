package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

import com.cooper.concert.domain.reservations.models.ConcertSeat;

public interface ConcertSeatCommandRepository {
	ConcertSeat findById(Long id);
	List<ConcertSeat> findAllByIdsAndSeatStatus(List<Long> concertSeatIds, String seatStatus);
}
