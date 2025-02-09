package com.cooper.concert.domain.reservations.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.request.ConcertSeatOccupyCancelRequest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleSeatsCacheRepository;

@Service
@RequiredArgsConstructor
public class ConcertScheduleSeatsCacheService {

	private final ConcertScheduleSeatsCacheRepository concertScheduleSeatsCacheRepository;

	public boolean existsConcertSeatsByConcertScheduleId(final Long concertScheduleId) {
		return concertScheduleSeatsCacheRepository.existsByConcertScheduleId(concertScheduleId);
	}

	public List<ConcertSeatResult> findAvailableSeatsByScheduleId(Long scheduleId) {
		return concertScheduleSeatsCacheRepository.findAvailableSeatsByScheduleId(scheduleId);
	}

	public Long saveAllConcertScheduleSeats(final Long concertScheduleId,
		final List<ConcertSeatResult> concertScheduleSeatsResult) {
		return concertScheduleSeatsCacheRepository.saveAllConcertScheduleSeats(concertScheduleId,
			concertScheduleSeatsResult);
	}

	public void updateToAvailableSeats(final List<ConcertSeatOccupyCancelRequest> concertSeatOccupyCancelRequests) {
		concertScheduleSeatsCacheRepository.updateToAvailableSeats(concertSeatOccupyCancelRequests);
	}
}
