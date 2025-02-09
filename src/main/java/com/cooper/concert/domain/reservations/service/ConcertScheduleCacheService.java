package com.cooper.concert.domain.reservations.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleCacheRepository;

@Service
@RequiredArgsConstructor
public class ConcertScheduleCacheService {

	private final ConcertScheduleCacheRepository concertScheduleCacheRepository;

	public ConcertScheduleResult findById(final Long concertId, final Long concertScheduleId) {
		return concertScheduleCacheRepository.findById(concertId, concertScheduleId);
	}

	public void saveConcertScheduleById(final Long concertId, final ConcertScheduleResult concertScheduleResult) {
		concertScheduleCacheRepository.saveConcertSchedule(concertId, concertScheduleResult);
	}
}
