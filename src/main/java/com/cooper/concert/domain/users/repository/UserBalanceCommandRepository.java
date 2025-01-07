package com.cooper.concert.domain.users.repository;

import com.cooper.concert.domain.users.models.UserBalance;

public interface UserBalanceCommandRepository {
	UserBalance findByUserId(final Long userId);
}