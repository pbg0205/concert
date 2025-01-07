package com.cooper.concert.domain.queues.infrastructure.rdb;

import static com.cooper.concert.domain.queues.models.QQueueToken.queueToken;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.service.repository.WaitingQueueCommandRepository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueCommandRepositoryDsl implements WaitingQueueCommandRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public QueueToken findByUserIdAndStatus(Long userId, String status) {
		return jpaQueryFactory.selectFrom(queueToken)
			.where(queueToken.userId.eq(userId).and(queueToken.status.stringValue().eq(status)))
			.fetchFirst();
	}

	@Override
	public List<QueueToken> findAllByIds(final List<Long> accessibleQueueTokenIds) {
		return jpaQueryFactory.selectFrom(queueToken)
			.where(queueToken.id.in(accessibleQueueTokenIds))
			.fetch();
	}

}
