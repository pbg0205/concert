package com.cooper.concert.interfaces.api.reservations.dto.response;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ConcertReservationResponse {
	private final UUID reservationId;
	private final UUID paymentId;
}
