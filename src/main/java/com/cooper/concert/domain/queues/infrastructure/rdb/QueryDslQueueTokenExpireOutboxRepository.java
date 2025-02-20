package com.cooper.concert.domain.queues.infrastructure.rdb;

import static com.cooper.concert.domain.queues.models.QQueueTokenExpireOutbox.queueTokenExpireOutbox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.infrastructure.redis.JpaQueueTokenOutboxRepository;
import com.cooper.concert.domain.queues.models.QueueTokenExpireOutbox;
import com.cooper.concert.domain.queues.models.QueueTokenOutboxStatus;
import com.cooper.concert.domain.queues.service.repository.QueueTokenExpireOutboxRepository;

@Repository
@RequiredArgsConstructor
public class QueryDslQueueTokenExpireOutboxRepository implements QueueTokenExpireOutboxRepository {

	private final JPAQueryFactory queryFactory;
	private final JpaQueueTokenOutboxRepository jpaQueueTokenOutboxRepository;

	@Override
	public QueueTokenExpireOutbox save(final QueueTokenExpireOutbox queueTokenExpireOutbox) {
		return jpaQueueTokenOutboxRepository.save(queueTokenExpireOutbox);
	}

	@Override
	public QueueTokenExpireOutbox findByPaymentId(final UUID paymentId) {
		return jpaQueueTokenOutboxRepository.findByPaymentId(paymentId);
	}

	@Override
	public List<QueueTokenExpireOutbox> findAllByRetryAtAndType(final LocalDateTime retryAt,
		final QueueTokenOutboxStatus status) {
		return queryFactory.selectFrom(queueTokenExpireOutbox)
			.where(queueTokenExpireOutbox.createdAt.after(retryAt).and(queueTokenExpireOutbox.type.eq(status.name())))
			.fetch();
	}
}
