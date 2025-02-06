package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class WaitingQueueRepositoryTest {

	@Autowired
	private WaitingQueueRepository waitingQueueRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void tearDown() {
		final Set<String> keys = redisTemplate.keys("*");
		redisTemplate.delete(keys);
	}

	@Test
	@DisplayName("대기열에 유저_아이디 등록 성공")
	void 대기열에_유저_아이디_등록_성공 () {
	    // given, when
		final Long position = waitingQueueRepository.enqueueUserId(1L);

		// then
		assertThat(position).isPositive();
	}

	@Test
	@DisplayName("대기열에 유저 아이디 존재하면 존재 확인 성공")
	void 대기열에_유저_아이디_존재하면_존재_확인_성공 () {
	    // given
		redisTemplate.opsForList().rightPush("waitingQueue", 1L);

		// when
		final boolean sut = waitingQueueRepository.existsUserId(1L);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("대기열에 유저 아이디 없으면 존재 확인 실패")
	void 대기열에_유저_아이디_없으면_존재_확인_실패 () {
	    // given
		redisTemplate.opsForList().rightPush("waitingQueue", 1L);

		// when
		final boolean sut = waitingQueueRepository.existsUserId(2L);

		// then
		assertThat(sut).isFalse();
	}

	@Test
	@DisplayName("대기열에 유저 아이디 존재하면 삭제 성공")
	void 대기열에_유저_아이디_존재하며_삭제_성공 () {
	    // given
		redisTemplate.opsForList().rightPush("waitingQueue", 1L);

		// when
		final boolean sut = waitingQueueRepository.removeUserId(1L);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("대기열에 유저 아이디 없을 경우 삭제 실패")
	void 대기열에_유저_아이디_없을_경우_삭제_실패 () {
	    // given
		redisTemplate.opsForList().rightPush("waitingQueue", 2L);

		// when
		final boolean sut = waitingQueueRepository.removeUserId(1L);

		// then
		assertThat(sut).isFalse();
	}

	@Test
	@DisplayName("대기열에서 사용자 제거 성공")
	void 대기열에서_사용자_제거_성공 () {
	    // given
		redisTemplate.opsForList().rightPush("waitingQueue", 2L);

		// when
		final List<Long> userIds = waitingQueueRepository.dequeueUserIds(1);

		// then
		assertThat(userIds).hasSize(1);
	}
}
