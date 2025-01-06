package com.cooper.concert.interfaces.presentation.users;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.business.dto.response.UserBalanceChargeResult;
import com.cooper.concert.business.service.UserBalanceService;
import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.interfaces.presentation.users.dto.request.UserBalanceReChargeRequest;
import com.cooper.concert.interfaces.presentation.users.dto.response.UserBalanceCheckResponse;
import com.cooper.concert.interfaces.presentation.users.dto.response.UserBalanceRechargeResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserBalanceController {

	private final UserBalanceService userBalanceService;

	@PostMapping("/balance/recharge")
	public ResponseEntity<ApiResponse<UserBalanceRechargeResponse>> rechargeBalance(
		@RequestBody final UserBalanceReChargeRequest userBalanceReChargeRequest) {

		final UserBalanceChargeResult userBalanceChargeResult = userBalanceService.chargePoint(
			userBalanceReChargeRequest.getUserId(),
			userBalanceReChargeRequest.getPoint());

		return ResponseEntity.ok()
			.body(ApiResponse.success(new UserBalanceRechargeResponse(userBalanceChargeResult.balance())));
	}

	@GetMapping("/{userId}/balance")
	public ResponseEntity<ApiResponse<UserBalanceCheckResponse>> getBalance(
		@PathVariable(name = "userId") final UUID userId) {
		return ResponseEntity.ok().body(ApiResponse.success(new UserBalanceCheckResponse(1000L)));
	}
}
