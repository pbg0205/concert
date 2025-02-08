package com.cooper.concert.domain.queues.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.jwt.QueueTokenGenerator;

@Service
@RequiredArgsConstructor
@Transactional
public class QueueTokenIssueService {

	private final QueueTokenGenerator queueTokenGenerator;

	public String issueQueueToken(final Long userId) {
		return queueTokenGenerator.generateJwt(userId, Instant.now());
	}

}
