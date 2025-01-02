package com.cooper.concert.payments.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.payments.presentation.dto.request.PaymentProcessRequest;
import com.cooper.concert.payments.presentation.dto.response.PaymentProcessResponse;

@RestController
@RequestMapping("/api")
public class PaymentProcessController {

	@PostMapping("/payments")
	public ResponseEntity<ApiResponse<PaymentProcessResponse>> processPayment(
		@RequestBody final PaymentProcessRequest paymentProcessRequest) {
		return ResponseEntity.ok().body(ApiResponse.success(new PaymentProcessResponse(40, UUID.randomUUID())));
	}
}
