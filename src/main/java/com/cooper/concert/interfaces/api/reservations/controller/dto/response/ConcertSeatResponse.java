package com.cooper.concert.interfaces.api.reservations.controller.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertSeatResponse {
	private final Long id;
	private final Long seatNumber;
}
