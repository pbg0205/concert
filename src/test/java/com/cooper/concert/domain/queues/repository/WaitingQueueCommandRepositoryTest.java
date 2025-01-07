package com.cooper.concert.domain.queues.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.repository.WaitingQueueCommandRepository;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.queues.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WaitingQueueCommandRepositoryTest {

	@Autowired
	private WaitingQueueCommandRepository waitingQueueCommandRepository;

	@Test
	@DisplayName("유저 식별자와 상태를 입력하면 유저 토큰을 반환 성공")
	@Sql("classpath:/sql/waiting_queue_position.sql")
	void 유저_식별자와_상태를_입력하면_유저_토큰_반환_성공() {
		// given
		final Long userId = 1007L;
		final String tokenStatus = QueueTokenStatus.WAITING.name();

		// when
		final QueueToken sut = waitingQueueCommandRepository.findByUserIdAndStatus(userId, tokenStatus);

		// then
		assertSoftly(softAssertions ->{
			softAssertions.assertThat(sut.getUserId()).isEqualTo(userId);
			softAssertions.assertThat(sut.getStatus().name()).isEqualTo(tokenStatus);
		});
	}
}
