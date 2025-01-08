package com.cooper.concert.interfaces.api.reservations.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertAvailableSeatsResponse;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertAvailableDateResponse;
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertScheduleReadUseCase;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertScheduleController {

	@Value("${concert.schedules.read.limit}")
	private int size;

	private final ConcertScheduleReadUseCase concertScheduleReadUseCase;

	@GetMapping("/{concertId}/available-dates")
	public ResponseEntity<ApiResponse<List<ConcertAvailableDateResponse>>> findAvailableDates(
		@PathVariable(name = "concertId") final Long concertId,
		@RequestParam(name = "page") final Integer page) {

		final Integer offset = (page - 1) * size;

		final List<ConcertScheduleResult> concertScheduleResults
			= concertScheduleReadUseCase.readAllScheduleByConcertIdAndPaging(concertId, offset, size);

		final List<ConcertAvailableDateResponse> concertAvailableDateResponses = concertScheduleResults.stream()
			.map(concertSchedule -> new ConcertAvailableDateResponse(
				concertSchedule.concertScheduleId(),
				concertSchedule.startDateTime().toLocalDate()))
			.toList();

		return ResponseEntity.ok()
			.body(ApiResponse.success(concertAvailableDateResponses));
	}

	@GetMapping("/{concertId}/{concertDate}/seats")
	public ResponseEntity<ApiResponse<ConcertAvailableSeatsResponse>> findAvailableSeats(
		@PathVariable(name = "concertId") final Long concertId,
		@PathVariable(name = "concertDate") final LocalDate concertDate,
		@RequestHeader("QUEUE-TOKEN") String tokenId) {
		return ResponseEntity.ok()
			.body(ApiResponse.success(new ConcertAvailableSeatsResponse(concertDate, List.of(1L, 2L, 3L))));
	}
}
