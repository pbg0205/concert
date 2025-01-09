package com.cooper.concert.domain.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.ConcertSeatStatus;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleSeatsResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertQueryRepository;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleQueryRepository;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertScheduleReadService {

	private final ConcertQueryRepository concertQueryRepository;
	private final ConcertScheduleQueryRepository concertScheduleQueryRepository;
	private final ConcertSeatQueryRepository concertSeatQueryRepository;

	public List<ConcertScheduleResult> findByAllByConcertIdAndPaging(
		final Long concertId, final Integer offset, final Integer limit) {
		if (!concertQueryRepository.existsById(concertId)) {
			throw new ConcertNotFoundException(ConcertErrorType.CONCERT_NOT_FOUND);
		}

		return concertScheduleQueryRepository.findByAllByConcertIdAndPaging(concertId, offset, limit);
	}

	public ConcertScheduleSeatsResult findAvailableSeatsByScheduleIdAndPaging(
		final Long scheduleId, final Integer offset, final Integer limit) {

		final ConcertScheduleResult concertScheduleResult =
			Optional.ofNullable(concertScheduleQueryRepository.findConcertScheduleResultById(scheduleId))
				.orElseThrow(() -> new ConcertScheduleNotFoundException(ConcertErrorType.CONCERT_SCHEDULE_NOT_FOUND));

		final List<ConcertSeatResult> availableSeats = concertSeatQueryRepository.findConcertSeatsByScheduleIdAndStatusAndPaging(
			scheduleId, ConcertSeatStatus.AVAILABLE.name(), offset, limit);

		return new ConcertScheduleSeatsResult(concertScheduleResult.startDateTime().toLocalDate(), availableSeats);
	}
}
