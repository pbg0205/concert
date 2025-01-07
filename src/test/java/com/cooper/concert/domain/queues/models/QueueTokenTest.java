package com.cooper.concert.domain.queues.models;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueueTokenTest {

	@Test
	@DisplayName("대기열 대기 토큰 생성 성공")
	void 대기열_대기_토큰_생성_성공() {
		// given
		final UUID tokenId = UUID.fromString("01943efe-ba03-7424-bba5-79c797d0a499"); // uuid v7
		final Long userId = 1L;

		// when
		final QueueToken sut = QueueToken.createWaitingToken(tokenId, 1L);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getTokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.getUserId()).isEqualTo(userId);
		});
	}
}
