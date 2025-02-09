package com.cooper.concert.domain.reservations.service.repository;

import java.util.List;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;

public interface ConcertSeatQueryRepository {
	List<ConcertSeatResult> findConcertSeatsByScheduleIdAndStatus(Long scheduleId, String status);

	ConcertSeatPriceInfo findConcertSeatPriceInfoById(Long seatId);
}
