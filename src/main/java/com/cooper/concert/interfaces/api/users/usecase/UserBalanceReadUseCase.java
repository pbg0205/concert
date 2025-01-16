package com.cooper.concert.interfaces.api.users.usecase;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.interfaces.components.annotations.Facade;
import com.cooper.concert.domain.users.service.UserBalanceReadService;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@Facade
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBalanceReadUseCase {

	private final UserReadService userReadService;
	private final UserBalanceReadService userBalanceReadService;

	public UserBalanceReadResult readUserBalance(final UUID userAltId) {
		final UserReadResult userReadResult = userReadService.findByAltId(userAltId);
		return userBalanceReadService.readUserBalance(userReadResult.userId());
	}
}
