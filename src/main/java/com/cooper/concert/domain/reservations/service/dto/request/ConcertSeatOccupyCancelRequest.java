package com.cooper.concert.domain.reservations.service.dto.request;

public record ConcertSeatOccupyCancelRequest(Long seatId, Long seatNumber, Long scheduleId) {
}
