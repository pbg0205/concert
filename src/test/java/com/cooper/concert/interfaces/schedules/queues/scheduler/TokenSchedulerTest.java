package com.cooper.concert.interfaces.schedules.queues.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.interfaces.schedules.queues.usecase.TokenSchedulerUseCase;

@SpringBootTest
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class TokenSchedulerTest {

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

		IntStream.range(0, userIds.size())
			.forEach(i -> {
				final Long userId = userIds.get(i);
				final LocalDateTime tokenExpiredAt = tokensExpiredAt.get(i);
				final String key = "activeTokens:userId" + ":" + userId;

				final ActiveQueueToken activeQueueToken = new ActiveQueueToken(userId, tokenExpiredAt);

				redisTemplate.opsForValue().set(key, activeQueueToken);
			});

		// when
		final Integer successCount = tokenSchedulerUseCase.expireToken(expiredAt);

		// then
		assertThat(successCount).isEqualTo(6);
	}

	@Test
	@DisplayName("토큰 예정 토큰 만료 상태 변경 성공")
	@Sql("classpath:sql/scheduler/token_expire_scheduler.sql")
	void 토큰_예정_토큰_만료_상태_변경_성공() {
		// given
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 7, 12, 10);

		// when
		final Integer successCount = tokenSchedulerUseCase.expireToken(expiredAt);

		// then
		assertThat(successCount).isEqualTo(2);
	}
}
