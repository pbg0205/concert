package com.cooper.concert.interfaces.api.reservations.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertReservationRequest {
	private final UUID token;
	private final LocalDate date;
	private final Integer seat;
}
