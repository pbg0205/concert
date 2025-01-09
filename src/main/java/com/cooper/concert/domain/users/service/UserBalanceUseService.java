package com.cooper.concert.domain.users.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserBalanceCommandRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBalanceUseService {

	private final UserBalanceCommandRepository userBalanceCommandRepository;

	public Long usePoint (final Long userId, final Long point) {
		final UserBalance userBalance = Optional.ofNullable(userBalanceCommandRepository.findByUserId(userId))
			.orElseThrow(() -> new UserBalanceNotFoundException(UserErrorType.USER_BALANCE_NOT_FOUND));
		return userBalance.usePoint(point);
	}
}
