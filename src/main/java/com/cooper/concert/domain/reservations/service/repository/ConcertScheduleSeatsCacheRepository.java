package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

import com.cooper.concert.domain.reservations.service.dto.request.ConcertSeatOccupyCancelRequest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;

public interface ConcertScheduleSeatsCacheRepository {
	boolean existsByConcertScheduleId(Long concertScheduleId);

	List<ConcertSeatResult> findAvailableSeatsByScheduleId(Long scheduleId);

	Long saveAllConcertScheduleSeats(Long concertScheduleId, final List<ConcertSeatResult> concertScheduleSeatsResult);

	void updateToAvailableSeats(List<ConcertSeatOccupyCancelRequest> concertSeatOccupyCancelRequests);

	void updateToUnavailableSeat(Long scheduleId, Long seatId, Long seatNumber);
}
