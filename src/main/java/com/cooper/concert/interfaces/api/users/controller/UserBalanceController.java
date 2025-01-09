package com.cooper.concert.interfaces.api.users.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;
import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.interfaces.api.users.dto.request.UserBalanceReChargeRequest;
import com.cooper.concert.interfaces.api.users.dto.response.UserBalanceCheckResponse;
import com.cooper.concert.interfaces.api.users.dto.response.UserBalanceRechargeResponse;
import com.cooper.concert.interfaces.api.users.usecase.UserBalanceChargeUseCase;
import com.cooper.concert.interfaces.api.users.usecase.UserBalanceReadUseCase;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserBalanceController {

	private final UserBalanceChargeUseCase userBalanceChargeUseCase;
	private final UserBalanceReadUseCase userBalanceReadUseCase;

	@PostMapping("/balance/recharge")
	public ResponseEntity<ApiResponse<UserBalanceRechargeResponse>> rechargeBalance(
		@RequestBody final UserBalanceReChargeRequest userBalanceReChargeRequest) {

		final UserBalanceChargeResult userBalanceChargeResult = userBalanceChargeUseCase.chargePoint(
			userBalanceReChargeRequest.getUserId(),
			userBalanceReChargeRequest.getPoint());

		return ResponseEntity.ok()
			.body(ApiResponse.success(new UserBalanceRechargeResponse(userBalanceChargeResult.balance())));
	}

	@GetMapping("/{userId}/balance")
	public ResponseEntity<ApiResponse<UserBalanceCheckResponse>> getBalance(
		@PathVariable(name = "userId") final UUID userAltId) {

		final UserBalanceReadResult userBalanceReadResult = userBalanceReadUseCase.readUserBalance(userAltId);

		return ResponseEntity.ok()
			.body(ApiResponse.success(new UserBalanceCheckResponse(userBalanceReadResult.balance())));
	}
}
