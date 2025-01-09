package com.cooper.concert.domain.reservations.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertSeatNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertSeatReadService {

	private final ConcertSeatQueryRepository concertSeatQueryRepository;

	public ConcertSeatPriceInfo findConcertSeatReadInfoById (final Long seatId) {
		return Optional.ofNullable(concertSeatQueryRepository.findConcertSeatPriceInfoById(seatId))
			.orElseThrow(() -> new ConcertSeatNotFoundException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));
	}
}
