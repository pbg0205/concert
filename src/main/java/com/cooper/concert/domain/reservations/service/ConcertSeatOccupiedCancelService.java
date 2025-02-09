package com.cooper.concert.domain.reservations.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.ConcertSeat;
import com.cooper.concert.domain.reservations.models.ConcertSeatStatus;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatOccupyCancelResult;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatCommandRepository;

@Service
@RequiredArgsConstructor
public class ConcertSeatOccupiedCancelService {

	private final ConcertSeatCommandRepository concertSeatCommandRepository;

	public List<ConcertSeatOccupyCancelResult> cancelOccupied(final List<Long> concertSeatIds) {
		final String seatStatus = ConcertSeatStatus.UNAVAILABLE.name();
		final List<ConcertSeat> concertSeats = concertSeatCommandRepository.findAllByIdsAndSeatStatus(concertSeatIds,
			seatStatus);
		return concertSeats.stream()
			.map(concertSeat -> {
				concertSeat.updateAvailable();
				return new ConcertSeatOccupyCancelResult(concertSeat.getId(), concertSeat.getSeatNumber(),
					concertSeat.getScheduleId());
			}).toList();
	}
}
