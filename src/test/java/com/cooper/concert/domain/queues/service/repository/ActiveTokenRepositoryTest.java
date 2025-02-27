package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.cooper.concert.domain.queues.service.dto.ActiveQueueToken;

@SpringBootTest
class ActiveTokenRepositoryTest {

	@Value("${token.processing.key.prefix}")
	private String activeTokenKeyPrefix;

	@Autowired
	private ActiveTokenRepository activeTokenRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void tearDown() {
		final Set<String> keys = redisTemplate.keys("*");
		redisTemplate.delete(keys);
	}

	@Test
	@DisplayName("활성화 토큰 등록 성공")
	void 활성화_토큰_등록_성공() {
		// given
		final List<Long> userIds = List.of(1L, 2L, 3L);
		final LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

		// when
		final Integer sut = activeTokenRepository.addActiveQueueTokens(userIds, expiredAt);

		// then
		assertThat(sut).isEqualTo(3);
	}

	@Test
	@DisplayName("활성화 토큰 갯수 조회 성공")
	void 활성화_토큰_갯수_조회_성공() {
		// given
		final List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
		final LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

		Map<Long, ActiveQueueToken> activeQueueTokenMap = new HashMap<>();
		for (Long userId : userIds) {
			activeQueueTokenMap.put(userId, new ActiveQueueToken(userId, expiredAt));
		}

		redisTemplate.opsForHash().putAll(activeTokenKeyPrefix, activeQueueTokenMap);

		// when
		final Integer activeTokenCount = activeTokenRepository.countActiveTokens();

		// then
		assertThat(activeTokenCount).isEqualTo(9);
	}

	@Test
	@DisplayName("활성화 토큰 삭제 성공")
	void 활성화_토큰_삭제_성공() {
		// given
		redisTemplate.opsForHash().put(activeTokenKeyPrefix, 1L, new ActiveQueueToken(1L, LocalDateTime.now()));

		// when
		final boolean sut = activeTokenRepository.removeActiveToken(1L);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("만료 토큰 제거 성공")
	void 만료_토큰_제거_성공() {
		// given
		Map<Long, ActiveQueueToken> activeQueueTokenMap = new HashMap<>();
		activeQueueTokenMap.put(1L, new ActiveQueueToken(1L, LocalDateTime.of(2025, 2, 5, 12, 0)));
		activeQueueTokenMap.put(2L, new ActiveQueueToken(2L, LocalDateTime.of(2025, 2, 5, 12, 5)));
		activeQueueTokenMap.put(3L, new ActiveQueueToken(3L, LocalDateTime.of(2025, 2, 5, 12, 10)));
		activeQueueTokenMap.put(4L, new ActiveQueueToken(4L, LocalDateTime.of(2025, 2, 5, 12, 15)));
		activeQueueTokenMap.put(5L, new ActiveQueueToken(5L, LocalDateTime.of(2025, 2, 5, 12, 25)));
		activeQueueTokenMap.put(6L, new ActiveQueueToken(6L, LocalDateTime.of(2025, 2, 5, 12, 30)));

		redisTemplate.opsForHash().putAll(activeTokenKeyPrefix, activeQueueTokenMap);

		// when
		final List<Long> sut = activeTokenRepository.removeAllLteExpiredAt(LocalDateTime.of(2025, 2, 5, 12, 15));

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).hasSize(3);
			softAssertions.assertThat(sut).containsAll(List.of(1L, 2L, 3L));
		});
	}
}
