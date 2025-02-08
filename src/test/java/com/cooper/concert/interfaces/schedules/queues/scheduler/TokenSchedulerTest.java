package com.cooper.concert.interfaces.schedules.queues.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.queues.service.dto.ActiveQueueToken;
import com.cooper.concert.interfaces.schedules.queues.usecase.TokenSchedulerUseCase;

@SpringBootTest
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class TokenSchedulerTest {

	@Value("${token.processing.key.prefix}")
	private String activeTokenKeyPrefix;

	@Autowired
	private TokenSchedulerUseCase tokenSchedulerUseCase;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void tearDown() {
		final Set<String> keys = this.redisTemplate.keys("*");
		this.redisTemplate.delete(keys);
	}

	@Test
	@DisplayName("토큰 활성화 상태 변경 성공")
	void 토큰_활성화_상태_변경_성공() {
		// given
		List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
		for (Long userId : userIds) {
			redisTemplate.opsForList().rightPush("waitingQueue", userId);
		}

		final LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

		// when
		final Integer successCount = tokenSchedulerUseCase.updateToProcessing(expiredAt);

		// then
		assertThat(successCount).isEqualTo(9);
	}

	@ParameterizedTest
	@MethodSource("tokenExpireMethodSource")
	@DisplayName("토큰 예정 토큰 만료 상태 변경 성공")
	@Sql("classpath:sql/scheduler/token_expire_scheduler.sql")
	void 토큰_예정_토큰_만료_상태_변경_성공(List<Long> userIds, List<LocalDateTime> tokensExpiredAt) {
		// given
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 7, 12, 10);

		Map<Long, ActiveQueueToken> activeTokensMap = new HashMap<>();
		for (int i = 0; i < userIds.size(); i++) {
			final Long userId = userIds.get(i);
			final LocalDateTime tokenExpiredAt = tokensExpiredAt.get(i);
			final ActiveQueueToken activeQueueToken = new ActiveQueueToken(userId, tokenExpiredAt);

			activeTokensMap.put(userId, activeQueueToken);
		}

		redisTemplate.opsForHash().putAll(activeTokenKeyPrefix, activeTokensMap);

		// when
		final Integer successCount = tokenSchedulerUseCase.expireToken(expiredAt);

		// then
		assertThat(successCount).isEqualTo(6);
	}

	private static Stream<Arguments> tokenExpireMethodSource() {
		return Stream.of(
			Arguments.arguments(
				List.of(1001L, 1002L, 1003L, 1004L, 1005L, 1006L),
				List.of(
					LocalDateTime.of(2025, 1, 7, 12, 0, 0),
					LocalDateTime.of(2025, 1, 7, 12, 0, 30),
					LocalDateTime.of(2025, 1, 7, 12, 1, 0),
					LocalDateTime.of(2025, 1, 7, 12, 1, 30),
					LocalDateTime.of(2025, 1, 7, 12, 2, 0),
					LocalDateTime.of(2025, 1, 7, 12, 2, 30)
				)
			)
		);
	}
}
