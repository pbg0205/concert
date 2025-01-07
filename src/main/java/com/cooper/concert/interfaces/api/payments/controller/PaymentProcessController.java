package com.cooper.concert.interfaces.api.payments.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.interfaces.api.payments.dto.request.PaymentProcessRequest;
import com.cooper.concert.interfaces.api.payments.dto.response.PaymentProcessResponse;

@RestController
@RequestMapping("/api")
public class PaymentProcessController {

	@PostMapping("/payments")
	public ResponseEntity<ApiResponse<PaymentProcessResponse>> processPayment(
		@RequestBody final PaymentProcessRequest paymentProcessRequest,
		@RequestHeader("QUEUE-TOKEN") String tokenId
		) {
		return ResponseEntity.ok().body(ApiResponse.success(new PaymentProcessResponse(40, UUID.randomUUID())));
	}
}
