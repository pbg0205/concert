package com.cooper.concert.users.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.users.presentation.dto.request.UserBalanceReChargeRequest;
import com.cooper.concert.users.presentation.dto.response.UserBalanceCheckResponse;
import com.cooper.concert.users.presentation.dto.response.UserBalanceRechargeResponse;

@RestController
@RequestMapping("/api/users")
public class UserBalanceController {

	@PostMapping("/balance/recharge")
	public ResponseEntity<ApiResponse<UserBalanceRechargeResponse>> rechargeBalance(
		@RequestBody final UserBalanceReChargeRequest userBalanceReChargeRequest) {
		return ResponseEntity.ok().body(ApiResponse.success(new UserBalanceRechargeResponse(1000L)));
	}

	@GetMapping("/{userId}/balance")
	public ResponseEntity<ApiResponse<UserBalanceCheckResponse>> getBalance(
		@PathVariable(name = "userId") final UUID userId) {
		return ResponseEntity.ok().body(ApiResponse.success(new UserBalanceCheckResponse(1000L)));
	}
}
