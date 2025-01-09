package com.cooper.concert.interfaces.api.reservations.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.cooper.concert.interfaces.api.reservations.controller.dto.response.ConcertSeatResponse;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertAvailableSeatsResponse {
	private final LocalDate date;
	private final List<ConcertSeatResponse> availableSeats;
}
