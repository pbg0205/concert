package com.cooper.concert.domain.users.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserQueryRepository;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {

	private final UserQueryRepository userQueryRepository;

	public UserReadResult findByAltId(final UUID userAltId) {
		return Optional.ofNullable(userQueryRepository.findByAltId(userAltId))
			.orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));
	}
}
