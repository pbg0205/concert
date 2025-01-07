package com.cooper.concert.domain.queues.infrastructure.rdb;

import static com.cooper.concert.domain.queues.models.QQueueToken.queueToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.repository.WaitingQueueQueryRepository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueQueryRepositoryDsl implements WaitingQueueQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long findTokenPositionByTokenId(final UUID tokenId, final String status) {
		Long targetId = queryFactory
			.select(queueToken.id)
			.from(queueToken)
			.where(queueToken.tokenId.eq(tokenId))
			.fetchOne();

		return queryFactory
			.select(queueToken.id.count().add(1L))
			.from(queueToken)
			.where(
				queueToken.status.stringValue().eq(status),
				queueToken.id.lt(targetId))
			.fetchOne();
	}

	public boolean existsTokenByUserIdAndStatus(final Long userId, String status) {
		return queryFactory
			.select(queueToken.id)
			.from(queueToken)
			.where(queueToken.userId.eq(userId).and(queueToken.status.stringValue().eq(status)))
			.fetchFirst() != null;
	}

	@Override
	public Integer countsTokenByStatusAndExpiredAt(final String status, LocalDateTime expiredAt) {
		return queryFactory.select(queueToken.id)
			.from(queueToken)
			.where(queueToken.status.stringValue().eq(status).and(queueToken.expiredAt.gt(expiredAt)))
			.fetch().size();
	}

	@Override
	public List<Long> findAccessibleIdsByStatusOrderByIdAsc(final String status, final Integer accessibleCount) {
		return queryFactory.select(queueToken.id)
			.from(queueToken)
			.where(queueToken.status.stringValue().eq(status))
			.orderBy(queueToken.id.asc())
			.limit(accessibleCount)
			.fetch();
	}
}
