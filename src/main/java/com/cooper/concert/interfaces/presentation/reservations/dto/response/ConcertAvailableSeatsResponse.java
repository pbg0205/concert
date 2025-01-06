package com.cooper.concert.interfaces.presentation.reservations.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertAvailableSeatsResponse {
	private final LocalDate date;
	private final List<Long> availableSeats;
}
