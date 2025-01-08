package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

public interface ConcertSeatQueryRepository {
	List<Long> findConcertSeatsByScheduleIdAndStatusAndPaging(
		Long scheduleId, String status, Integer offset, Integer limit);
}
