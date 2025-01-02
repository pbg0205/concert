package com.cooper.concert.reservations.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.reservations.presentation.dto.response.ConcertAvailableSeatsResponse;
import com.cooper.concert.reservations.presentation.dto.response.ConcertAvailableDateResponse;

@RestController
@RequestMapping("/api/concert")
public class ConcertScheduleController {

	@GetMapping("/{concertId}/available-dates")
	public ResponseEntity<ApiResponse<ConcertAvailableDateResponse>> findAvailableDates(
		@PathVariable(name = "concertId") final Long concertId) {
		return ResponseEntity.ok()
			.body(ApiResponse.success(new ConcertAvailableDateResponse(List.of(LocalDate.of(2025, 1, 5)))));
	}

	@GetMapping("/{concertId}/{concertDate}/seats")
	public ResponseEntity<ApiResponse<ConcertAvailableSeatsResponse>> findAvailableSeats(
		@PathVariable(name = "concertId") final Long concertId,
		@PathVariable(name = "concertDate") final LocalDate concertDate) {
		return ResponseEntity.ok()
			.body(ApiResponse.success(new ConcertAvailableSeatsResponse(concertDate, List.of(1L, 2L, 3L))));
	}
}
