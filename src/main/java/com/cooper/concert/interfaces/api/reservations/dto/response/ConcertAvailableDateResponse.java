package com.cooper.concert.interfaces.api.reservations.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertAvailableDateResponse {
	private final Long concertScheduleId;
	private final LocalDate availableDate;
}
