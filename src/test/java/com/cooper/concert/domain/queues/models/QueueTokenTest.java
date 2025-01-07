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

	@Test
	@DisplayName("대기열 대기 토큰이 대기 상태이면 취소 상태 변경 성공")
	void 대기열_대기_토큰이_대기_상태이면_취소_상태_변경_성공() {
		// given
		final UUID tokenId = UUID.fromString("01943efe-ba03-7424-bba5-79c797d0a499"); // uuid v7

		// when
		final QueueToken sut = QueueToken.createWaitingToken(tokenId, 1L);
		sut.updateCanceled();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getTokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.CANCELLED);
		});
	}

	@Test
	@DisplayName("대기열 대기 토큰이 대기 상태이면 활성화 상태 변경 성공")
	void 대기열_대기_토큰이_대기_상태이면_활성화_상태_변경_성공() {
		// given
		final UUID tokenId = UUID.fromString("01943efe-ba03-7424-bba5-79c797d0a499"); // uuid v7

		// when
		final QueueToken sut = QueueToken.createWaitingToken(tokenId, 1L);
		sut.updateProcessing();

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getTokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.PROCESSING);
		});
	}
}
