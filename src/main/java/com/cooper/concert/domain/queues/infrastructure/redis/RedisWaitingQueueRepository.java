package com.cooper.concert.domain.queues.infrastructure.redis;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.repository.WaitingQueueRepository;

@Repository
@RequiredArgsConstructor
public class RedisWaitingQueueRepository implements WaitingQueueRepository {

	@Value("${waiting-queue.name}")
	private String waitingQueueName;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long enqueueUserId(final Long userId) {
		return redisTemplate.opsForList().rightPush(waitingQueueName, userId);
	}

	@Override
	public boolean existsUserId(final Long userId) {
		final Long index = redisTemplate.opsForList().indexOf(waitingQueueName, userId);
		return Objects.nonNull(index);
	}

	@Override
	public boolean removeUserId(final Long userId) {
		return redisTemplate.opsForList().remove(waitingQueueName, 0, userId) > 0;
	}

	@Override
	public List<Long> dequeueUserIds(final Integer availableActiveTokenCount) {
		final List<Long> userIds = redisTemplate.opsForList().range(waitingQueueName, 0, availableActiveTokenCount - 1)
			.stream()
			.map(obj -> Long.parseLong(obj.toString()))
			.toList();

		redisTemplate.opsForList().trim(waitingQueueName, availableActiveTokenCount, -1);
		return userIds;
	}
}
