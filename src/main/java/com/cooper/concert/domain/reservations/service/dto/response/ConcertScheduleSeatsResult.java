package com.cooper.concert.domain.reservations.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ConcertScheduleSeatsResult(LocalDate date, List<ConcertSeatResult> availableSeats) {
}
