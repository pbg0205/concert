package com.cooper.concert.domain.reservations.service.dto.response;

import java.util.UUID;

public record ConcertReservationResult(UUID reservationAltId, UUID paymentAltId) {
}
