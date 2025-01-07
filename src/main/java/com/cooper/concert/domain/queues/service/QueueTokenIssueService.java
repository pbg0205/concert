package com.cooper.concert.domain.queues.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.generator.TokenIdGenerator;
import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.repository.QueueTokenCommandRepository;
import com.cooper.concert.domain.queues.service.dto.response.QueueTokenIssueInfo;

@Service
@RequiredArgsConstructor
@Transactional
public class QueueTokenIssueService {

	private final TokenIdGenerator tokenIdGenerator;
	private final QueueTokenCommandRepository queueTokenCommandRepository;

	public QueueTokenIssueInfo issueQueueToken(final Long userId) {
		final UUID tokenId = tokenIdGenerator.generateTokenId();
		final QueueToken waitingToken = queueTokenCommandRepository.save(QueueToken.createWaitingToken(tokenId, userId));
		return new QueueTokenIssueInfo(waitingToken.getTokenId());
	}

}
