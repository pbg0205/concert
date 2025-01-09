package com.cooper.concert.domain.reservations.service.dto.response;

import java.util.UUID;

public record ConcertReservationCompletedInfo(Long id, Long userId, Long seatId, UUID altId) {
}
