package com.cooper.concert.domain.queues.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.QueueTokenQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueueTokenReadService {

	private final QueueTokenQueryRepository queueTokenQueryRepository;

	public Long findUserIdByTokenId(final UUID tokenId) {
		return Optional.ofNullable(queueTokenQueryRepository.findUserIdByTokenId(tokenId))
			.orElseThrow(() -> new TokenNotFoundException(TokenErrorType.TOKEN_NOT_FOUND));
	}
}
