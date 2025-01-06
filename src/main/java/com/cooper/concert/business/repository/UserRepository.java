package com.cooper.concert.business.repository;

import java.util.UUID;

import com.cooper.concert.domain.User;

public interface UserRepository {
	User findByAltId(UUID altId);
}
