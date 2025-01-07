package com.cooper.concert.domain.queues.infrastructure.rdb;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.repository.QueueTokenCommandRepository;

@Repository
@RequiredArgsConstructor
public class QueueTokenCommandRepositoryDsl implements QueueTokenCommandRepository {

	private final JpaQueueTokenRepository jpaQueueTokenRepository;

	@Override
	public QueueToken save(final QueueToken queueToken) {
		return jpaQueueTokenRepository.save(queueToken);
	}
}
