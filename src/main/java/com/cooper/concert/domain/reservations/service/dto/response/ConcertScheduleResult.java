package com.cooper.concert.domain.reservations.service.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ConcertScheduleResult(
	Long concertScheduleId, LocalDateTime startDateTime) implements Serializable {
}
