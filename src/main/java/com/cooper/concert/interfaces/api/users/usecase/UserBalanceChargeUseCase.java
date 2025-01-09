package com.cooper.concert.interfaces.api.users.usecase;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Facade;
import com.cooper.concert.domain.users.service.UserBalanceChargeService;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@Facade
@RequiredArgsConstructor
@Transactional
public class UserBalanceChargeUseCase {
	private final UserReadService userReadService;
	private final UserBalanceChargeService userBalanceChargeService;

	public UserBalanceChargeResult chargePoint(final UUID userAltId, final Long point) {
		final UserReadResult userReadResult = userReadService.findByAltId(userAltId);
		return userBalanceChargeService.chargePoint(userReadResult.userId(), point);
	}

}
