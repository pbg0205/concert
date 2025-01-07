package com.cooper.concert.domain.users.service.repository;

import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;

public interface UserBalanceQueryRepository {
	UserBalanceReadResult findByUserId(final Long userId);
}
