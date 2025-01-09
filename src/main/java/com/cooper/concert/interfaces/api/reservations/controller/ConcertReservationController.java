package com.cooper.concert.interfaces.api.reservations.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.api.support.response.ApiResponse;
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
		@RequestHeader("QUEUE-TOKEN") String tokenIdStr,
		@RequestBody final ConcertReservationRequest concertReservationRequest
	) {
		UUID tokenId = UUID.fromString(tokenIdStr);

		final ConcertReservationResult concertReservationResult =
			concertReservationUseCase.reserveConcertSeat(tokenId, concertReservationRequest.getSeatId());

		final ConcertReservationResponse concertReservationResponse = new ConcertReservationResponse(
			concertReservationResult.reservationAltId(),
			concertReservationResult.paymentAltId());

		return ResponseEntity.ok()
			.body(ApiResponse.success(concertReservationResponse));
	}
}
