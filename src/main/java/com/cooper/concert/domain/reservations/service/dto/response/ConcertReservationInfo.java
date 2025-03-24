package com.cooper.concert.domain.reservations.service.dto.response;

import java.util.UUID;

public record ConcertReservationInfo(Long reservationId, UUID reservationAltId, Long scheduleId, Long seatNumber) {
}
