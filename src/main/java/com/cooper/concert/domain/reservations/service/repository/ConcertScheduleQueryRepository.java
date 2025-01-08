package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;

public interface ConcertScheduleQueryRepository {
	List<ConcertScheduleResult> findByAllByConcertIdAndPaging(Long concertId, int page, int size);
}
