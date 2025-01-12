package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.queues.models.QueueToken;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.queues.infrastructure.rdb",
	"com.cooper.concert.common.jpa"})
class QueueTokenStoreRepositoryTest {

	@Autowired
	private QueueTokenCommandRepository queueTokenCommandRepository;

	@Test
	@DisplayName("대기열 토큰 저장 성공")
	void 대기열_토큰_저장_성공() {
		// given
		final QueueToken waitingToken = QueueToken.createWaitingToken(UUID.randomUUID(), 1L);

		// when
		final QueueToken sut = queueTokenCommandRepository.save(waitingToken);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getTokenId()).isNotNull();
			softAssertions.assertThat(sut)
				.extracting("status")
				.satisfies(status -> softAssertions.assertThat(status.toString()).isEqualTo("WAITING"));
		});
	}
}
