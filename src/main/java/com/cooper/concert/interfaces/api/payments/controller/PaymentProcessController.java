package com.cooper.concert.interfaces.api.payments.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.components.annotations.QueueToken;
import com.cooper.concert.api.components.dto.TokenHeaderData;
import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.payments.service.dto.response.PaymentProcessResult;
import com.cooper.concert.interfaces.api.payments.dto.request.PaymentProcessRequest;
import com.cooper.concert.interfaces.api.payments.dto.response.PaymentProcessResponse;
import com.cooper.concert.interfaces.api.payments.usecase.PaymentProcessUseCase;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentProcessController {

	private final PaymentProcessUseCase processUseCase;

	@PostMapping("/payments")
	public ResponseEntity<ApiResponse<PaymentProcessResponse>> processPayment(
		@QueueToken TokenHeaderData tokenHeader,
		@RequestBody final PaymentProcessRequest paymentProcessRequest) {

		final PaymentProcessResult paymentProcessResult = processUseCase.processPayment(
			paymentProcessRequest.getPaymentId(), tokenHeader.getTokenId());

		return ResponseEntity.ok()
			.body(ApiResponse.success(new PaymentProcessResponse(paymentProcessResult.reservationAltId())));
	}
}
