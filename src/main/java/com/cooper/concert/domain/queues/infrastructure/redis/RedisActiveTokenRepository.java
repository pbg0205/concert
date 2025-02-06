package com.cooper.concert.domain.queues.infrastructure.redis;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.dto.ActiveQueueToken;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@Repository
@RequiredArgsConstructor
public class RedisActiveTokenRepository implements ActiveTokenRepository {

	@Value("${token.processing.key.prefix}")
	private String activeTokenKeyPrefix;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Integer addActiveQueueTokens(final List<Long> userIds, final LocalDateTime expiredAt) {
		Integer count = 0;
		Map<Long, Object> activeQueueTokenMap = new HashMap<>();

		for (Long userId : userIds) {
			final ActiveQueueToken activeQueueToken = new ActiveQueueToken(userId, expiredAt);
			activeQueueTokenMap.put(userId, activeQueueToken);
			count++;
		}

		redisTemplate.opsForHash().putAll(activeTokenKeyPrefix, activeQueueTokenMap);

		return count;
	}

	@Override
	public Integer countActiveTokens() {
		final Set<Object> activeTokenKeys = redisTemplate.opsForHash().keys(activeTokenKeyPrefix);
		return activeTokenKeys.size();
	}

	@Override
	public boolean existsByUserId(final Long userId) {
		return redisTemplate.opsForHash().hasKey(activeTokenKeyPrefix, userId);
	}

	@Override
	public boolean removeActiveToken(final Long userId) {
		return redisTemplate.opsForHash().delete(activeTokenKeyPrefix, userId) > 0;
	}

	@Override
	public List<Long> removeAllLteExpiredAt(final LocalDateTime expiredAt) {
		final Set<Object> keys = redisTemplate.opsForHash().keys(activeTokenKeyPrefix);
		final List<Long> userIds = redisTemplate.opsForHash().multiGet(activeTokenKeyPrefix, keys)
			.stream()
			.map(obj -> (ActiveQueueToken) obj)
			.filter(activeQueueToken -> activeQueueToken.expiredAt().isBefore(expiredAt))
			.map(ActiveQueueToken::userId)
			.toList();

		redisTemplate.opsForHash().delete(activeTokenKeyPrefix, userIds);

		return userIds;
	}

}
