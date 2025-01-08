package com.cooper.concert.domain.reservations.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertQueryRepository;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertScheduleReadService {

	private final ConcertQueryRepository concertQueryRepository;
	private final ConcertScheduleQueryRepository concertScheduleQueryRepository;

	public List<ConcertScheduleResult> findByAllByConcertIdAndPaging(
		final Long concertId, final Integer offset, final Integer limit) {
		if (!concertQueryRepository.existsById(concertId)) {
			throw new ConcertNotFoundException(ConcertErrorType.CONCERT_NOT_FOUND);
		}

		return concertScheduleQueryRepository.findByAllByConcertIdAndPaging(concertId, offset, limit);
	}
}
