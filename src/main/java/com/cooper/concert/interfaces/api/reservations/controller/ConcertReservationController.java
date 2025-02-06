package com.cooper.concert.interfaces.api.reservations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.components.annotations.QueueToken;
import com.cooper.concert.api.components.dto.TokenHeaderData;
import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationResult;
import com.cooper.concert.interfaces.api.reservations.dto.request.ConcertReservationRequest;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertReservationResponse;
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertReservationUseCase;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertReservationController {

	private final ConcertReservationUseCase concertReservationUseCase;

	@PostMapping("/seats/reservation")
	public ResponseEntity<ApiResponse<ConcertReservationResponse>> reserveConcertSeats(
		@QueueToken TokenHeaderData tokenHeader,
		@RequestBody final ConcertReservationRequest concertReservationRequest
	) {
		final ConcertReservationResult concertReservationResult =
			concertReservationUseCase.reserveConcertSeat(tokenHeader.userId(), concertReservationRequest.getSeatId());

		final ConcertReservationResponse concertReservationResponse = new ConcertReservationResponse(
			concertReservationResult.reservationAltId(),
			concertReservationResult.paymentAltId());

		return ResponseEntity.ok()
			.body(ApiResponse.success(concertReservationResponse));
	}
}
