package com.cooper.concert.business.repository;

import com.cooper.concert.domain.UserBalance;

public interface UserBalanceRepository {
	UserBalance findByUserId(final Long userId);
}
