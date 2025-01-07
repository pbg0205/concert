package com.cooper.concert.domain.users.repository;

import java.util.UUID;

import com.cooper.concert.domain.users.models.User;

public interface UserRepository {
	User findByAltId(UUID altId);
}
