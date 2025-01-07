package com.cooper.concert.domain.users.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.repository.UserBalanceCommandRepository;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBalanceChargeService {
	private final UserBalanceCommandRepository userBalanceCommandRepository;

	public UserBalanceChargeResult chargePoint(final Long userId, final Long point) {
		final UserBalance userBalance = Optional.ofNullable(userBalanceCommandRepository.findByUserId(userId))
			.orElseThrow(() -> new UserBalanceNotFoundException(UserErrorType.USER_BALANCE_NOT_FOUND));

		final Long availableBalance = userBalance.addPoint(point);
		return new UserBalanceChargeResult(availableBalance);
	}

	// TODO 테스트 수정 후, 제거
	@Transactional(readOnly = true)
	public UserBalanceReadResult readUserBalance(final Long userId) {
		final UserBalance userBalance = Optional.ofNullable(userBalanceCommandRepository.findByUserId(userId))
			.orElseThrow(() -> new UserBalanceNotFoundException(UserErrorType.USER_BALANCE_NOT_FOUND));

		return new UserBalanceReadResult(userBalance.getPoint());
	}
}