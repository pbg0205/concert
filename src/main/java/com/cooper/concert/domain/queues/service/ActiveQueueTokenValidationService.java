package com.cooper.concert.domain.queues.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveQueueTokenValidationService {

	private final QueueTokenExtractor queueTokenExtractor;
	private final ActiveTokenRepository activeTokenRepository;

	public boolean validateActiveQueueToken(final String token) {
		final Long userId = queueTokenExtractor.extractUserIdFromToken(token);
		return activeTokenRepository.existsByUserId(userId);
	}
}
