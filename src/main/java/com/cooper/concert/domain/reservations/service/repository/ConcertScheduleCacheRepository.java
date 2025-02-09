package com.cooper.concert.domain.reservations.service.repository;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;

public interface ConcertScheduleCacheRepository {
	ConcertScheduleResult findById(final Long concertId, Long concertScheduleId);

	void saveConcertSchedule(Long concertId, ConcertScheduleResult concertScheduleId);
}
