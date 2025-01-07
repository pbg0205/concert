package com.cooper.concert.domain.users.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.repository.UserQueryRepository;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@Service
@RequiredArgsConstructor
public class UserReadService {

	private final UserQueryRepository userQueryRepository;

	public UserReadResult findByAltId(final UUID userAltId) {
		return Optional.ofNullable(userQueryRepository.findByAltId(userAltId))
			.orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));
	}
}
