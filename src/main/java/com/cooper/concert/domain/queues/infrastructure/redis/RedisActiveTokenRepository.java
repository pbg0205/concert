package com.cooper.concert.domain.queues.infrastructure.redis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
		for (Long userId : userIds) {
			final String tokenKey = activeTokenKeyPrefix + userId;
			final ActiveQueueToken activeQueueToken = new ActiveQueueToken(userId, expiredAt);

			redisTemplate.opsForValue().set(tokenKey, activeQueueToken);
			count++;
		}

		return count;
	}

	@Override
	public Integer countActiveTokens() {
		final Set<String> activeTokenKeys = redisTemplate.keys(activeTokenKeyPrefix + "*");
		return activeTokenKeys.size();
	}

	@Override
	public boolean existsByUserId(final Long userId) {
		return redisTemplate.opsForValue().get(activeTokenKeyPrefix + userId) != null;
	}

	@Override
	public boolean removeActiveToken(final Long userId) {
		return Boolean.TRUE.equals(redisTemplate.delete(activeTokenKeyPrefix + userId));
	}

	@Override
	public List<Long> removeAllLteExpiredAt(final LocalDateTime expiredAt) {
		final Set<String> keys = redisTemplate.keys(activeTokenKeyPrefix + "*");

		final List<Long> userIds = redisTemplate.opsForValue().multiGet(keys)
			.stream()
			.map(obj -> (ActiveQueueToken)obj)
			.filter(activeQueueToken -> activeQueueToken.expiredAt().isBefore(expiredAt))
			.map(ActiveQueueToken::userId)
			.toList();

		List<String> expiredKeys = new ArrayList<>();
		for (Long userId : userIds) {
			expiredKeys.add(activeTokenKeyPrefix + userId);
		}

		redisTemplate.delete(expiredKeys);

		return userIds;
	}

}
