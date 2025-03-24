package com.cooper.concert.domain.reservations.service.dto.event;

public record SeatCacheUnavailableEvent(Long scheduleId, Long seatId, Long seatNumber) {
}
