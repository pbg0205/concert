package com.cooper.concert.interfaces.api.reservations.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.interfaces.api.reservations.dto.request.ConcertReservationRequest;
import com.cooper.concert.interfaces.api.reservations.dto.response.ConcertReservationResponse;

@RestController
@RequestMapping("/api/concert")
public class ConcertReservationController {

	@PostMapping("/seats/reservation")
	public ResponseEntity<ApiResponse<ConcertReservationResponse>> reserveConcertSeats(
		@RequestBody final ConcertReservationRequest concertReservationRequest,
		@RequestHeader("QUEUE-TOKEN") String tokenId
		) {
		return ResponseEntity.ok()
			.body(ApiResponse.success(new ConcertReservationResponse(
				UUID.randomUUID(),
				UUID.randomUUID(),
				concertReservationRequest.getDate(),
				concertReservationRequest.getSeat()))
			);
	}
}
