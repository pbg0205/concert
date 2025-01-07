package com.cooper.concert.domain.users.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserBalanceQueryRepository;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBalanceReadService {

	private final UserBalanceQueryRepository userBalanceQueryRepository;

	public UserBalanceReadResult readUserBalance(final Long userId) {
		return Optional.ofNullable(userBalanceQueryRepository.findByUserId(userId))
			.orElseThrow(() -> new UserBalanceNotFoundException(UserErrorType.USER_BALANCE_NOT_FOUND));
	}
}
