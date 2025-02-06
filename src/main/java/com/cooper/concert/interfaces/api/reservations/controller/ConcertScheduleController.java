package com.cooper.concert.interfaces.api.reservations.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleSeatsResult;
import com.cooper.concert.interfaces.api.reservations.controller.dto.response.ConcertSeatResponse;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertAvailableDateResponse;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertAvailableSeatsResponse;
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertScheduleReadUseCase;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertScheduleController {

	@Value("${concert.schedules.read.limit}")
	private int scheduleSize;

	private final ConcertScheduleReadUseCase concertScheduleReadUseCase;

	@GetMapping("/{concertId}/available-dates")
	public ResponseEntity<ApiResponse<List<ConcertAvailableDateResponse>>> findAvailableDates(
		@PathVariable(name = "concertId") final Long concertId,
		@RequestParam(name = "page") final Integer page) {

		final Integer offset = (page - 1) * scheduleSize;

		final List<ConcertScheduleResult> concertScheduleResults
			= concertScheduleReadUseCase.readAllScheduleByConcertIdAndPaging(concertId, offset, scheduleSize);

		final List<ConcertAvailableDateResponse> concertAvailableDateResponses = concertScheduleResults.stream()
			.map(concertSchedule -> new ConcertAvailableDateResponse(
				concertSchedule.concertScheduleId(),
				concertSchedule.startDateTime().toLocalDate()))
			.toList();

		return ResponseEntity.ok()
			.body(ApiResponse.success(concertAvailableDateResponses));
	}

	@GetMapping("/{concertScheduleId}/seats")
	public ResponseEntity<ApiResponse<ConcertAvailableSeatsResponse>> findAvailableSeats(
		@PathVariable(name = "concertScheduleId") final Long concertScheduleId) {

		final ConcertScheduleSeatsResult concertScheduleSeatsResult =
			concertScheduleReadUseCase.readAvailableSeatsByScheduleId(concertScheduleId);

		final LocalDate concertDate = concertScheduleSeatsResult.date();
		final List<ConcertSeatResponse> availableSeats =
			concertScheduleSeatsResult.availableSeats().stream()
				.map(result -> new ConcertSeatResponse(result.id(), result.seatNumber()))
				.toList();

		ConcertAvailableSeatsResponse concertScheduleSeatsResponse =
			new ConcertAvailableSeatsResponse(concertDate, availableSeats);

		return ResponseEntity.ok()
			.body(ApiResponse.success(concertScheduleSeatsResponse));
	}
}
