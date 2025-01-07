package com.cooper.concert.domain.queues.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.repository.WaitingQueueQueryRepository;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.queues.infrastructure.rdb", "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WaitingQueueQueryRepositoryTest {

	@Autowired
	private WaitingQueueQueryRepository waitingQueueQueryRepository;

	@Test
	@DisplayName("대기열 순서 조회 성공")
	@Sql("classpath:sql/waiting_queue_position.sql")
	void 대기열_순서_조회_성공() {
		// given
		final UUID tokenId = UUID.fromString("01b8f8a1-6f8c-7b6e-87c3-234a3c15f804");

		// when
		final Long sut = waitingQueueQueryRepository.findTokenPositionByTokenId(tokenId, "WAITING");

		// then
		assertThat(sut).isEqualTo(4);
	}

	@Test
	@DisplayName("사용 가능한 대기 토큰 존재여부 성공")
	@Sql("classpath:sql/waiting_queue_position.sql")
	void 사용_가능한_대기_토큰_존재여부_성공() {
		// given
		final Long userId = 1007L; // WAITING 대기열 토믄
		final String tokenStatus = QueueTokenStatus.WAITING.name();

		// when
		final boolean sut = waitingQueueQueryRepository.existsTokenByUserIdAndStatus(userId, tokenStatus);

		// then
		assertThat(sut).isTrue();
	}
}
