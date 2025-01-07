package com.cooper.concert.domain.users.repository;

import java.util.UUID;

import com.cooper.concert.domain.users.service.response.UserReadResult;

public interface UserQueryRepository {
	UserReadResult findByAltId(UUID altId);
}
