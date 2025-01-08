package com.cooper.concert.domain.users.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserBalanceCommandRepository;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;

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
}
