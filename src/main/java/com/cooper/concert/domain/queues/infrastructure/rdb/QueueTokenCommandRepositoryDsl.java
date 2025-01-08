package com.cooper.concert.domain.queues.infrastructure.rdb;

import static com.cooper.concert.domain.queues.models.QQueueToken.queueToken;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.service.repository.QueueTokenCommandRepository;

@Repository
@RequiredArgsConstructor
@Transactional
public class QueueTokenCommandRepositoryDsl implements QueueTokenCommandRepository {

	private final JpaQueueTokenRepository jpaQueueTokenRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public QueueToken save(final QueueToken queueToken) {
		return jpaQueueTokenRepository.save(queueToken);
	}

	@Override
	public List<QueueToken> findAllByStatus(final String status) {
		return queryFactory.selectFrom(queueToken)
			.where(queueToken.status.stringValue().eq(status))
			.fetch();
	}
}
