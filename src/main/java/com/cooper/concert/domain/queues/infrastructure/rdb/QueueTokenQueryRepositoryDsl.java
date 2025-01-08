package com.cooper.concert.domain.queues.infrastructure.rdb;

import static com.cooper.concert.domain.queues.models.QQueueToken.queueToken;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.service.repository.QueueTokenQueryRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueueTokenQueryRepositoryDsl implements QueueTokenQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public QueueToken findByTokenId(final UUID tokenId) {
		return queryFactory.selectFrom(queueToken)
			.where(queueToken.tokenId.eq(tokenId))
			.fetchOne();
	}

}
