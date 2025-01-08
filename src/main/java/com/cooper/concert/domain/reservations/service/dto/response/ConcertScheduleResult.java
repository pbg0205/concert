package com.cooper.concert.domain.reservations.service.dto.response;

import java.time.LocalDateTime;

public record ConcertScheduleResult(
	Long concertScheduleId, LocalDateTime startDateTime) {
}
