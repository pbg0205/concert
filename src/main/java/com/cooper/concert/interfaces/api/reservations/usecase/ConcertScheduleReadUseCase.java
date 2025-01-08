package com.cooper.concert.interfaces.api.reservations.usecase;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Facade;
import com.cooper.concert.domain.reservations.service.ConcertScheduleReadService;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;

@Facade
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertScheduleReadUseCase {

	private final ConcertScheduleReadService concertScheduleReadService;

	public List<ConcertScheduleResult> readAllScheduleByConcertIdAndPaging(
		final Long concertId, final Integer offset, final Integer limit) {
		return concertScheduleReadService.findByAllByConcertIdAndPaging(concertId, offset, limit);
	}

}
