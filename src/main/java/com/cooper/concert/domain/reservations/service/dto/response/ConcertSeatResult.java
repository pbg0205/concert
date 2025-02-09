package com.cooper.concert.domain.reservations.service.dto.response;

import java.io.Serializable;

public record ConcertSeatResult(Long id, Long seatNumber) implements Serializable {
}
