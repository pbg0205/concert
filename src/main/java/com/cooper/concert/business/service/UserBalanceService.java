package com.cooper.concert.business.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.business.dto.response.UserBalanceChargeResult;
import com.cooper.concert.business.errors.UserErrorType;
import com.cooper.concert.business.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.business.errors.exception.UserNotFoundException;
import com.cooper.concert.business.repository.UserBalanceRepository;
import com.cooper.concert.business.repository.UserRepository;
import com.cooper.concert.domain.User;
import com.cooper.concert.domain.UserBalance;

@Service
@RequiredArgsConstructor
public class UserBalanceService {
	private final UserRepository userRepository;
	private final UserBalanceRepository userBalanceRepository;

	@Transactional
	public UserBalanceChargeResult chargePoint(final UUID userAltId, final Long point) {
		final User user = Optional.ofNullable(userRepository.findByAltId(userAltId))
			.orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));

		final UserBalance userBalance = Optional.ofNullable(userBalanceRepository.findByUserId(user.getId()))
			.orElseThrow(() -> new UserBalanceNotFoundException(UserErrorType.USER_BALANCE_NOT_FOUND));

		final Long availableBalance = userBalance.addPoint(point);
		return new UserBalanceChargeResult(availableBalance);
	}
}
