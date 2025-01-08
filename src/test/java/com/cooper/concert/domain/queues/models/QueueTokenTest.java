package com.cooper.concert.domain.queues.models;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
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
		final int validTokenProcessingMinutes = 5;
		final LocalDateTime currentTime = LocalDateTime.now();

		// when
		final QueueToken sut = QueueToken.createWaitingToken(tokenId, 1L);
		sut.updateProcessing(currentTime, validTokenProcessingMinutes);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getTokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.PROCESSING);
			softAssertions.assertThat(sut.getExpiredAt()).isEqualTo(currentTime.plusMinutes(validTokenProcessingMinutes));
		});
	}

	@Test
	@DisplayName("활성 상태가 아닐 경우 만료 상태 변경 실패")
	void 활성_상태가_아닐_경우_만료_상태_변경_실패() {
		// given
		UUID tokenId = UUID.fromString("01944407-23dc-7806-b923-fe3f1faa5e4a"); //UUID ver 7
		final Long userId = 1L;
		final LocalDateTime currentTime = LocalDateTime.now();

		final QueueToken sut = QueueToken.createWaitingToken(tokenId, userId);
		// sut.updateProcessing(currentTime, 5);

		// when
		final boolean expired = sut.expire(currentTime.plusMinutes(6));

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(expired).isFalse();
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.WAITING);
		});
	}

	@Test
	@DisplayName("활성 상태이고 만료시간 미만인 경우 만료상태 변경 실패")
	void 활성_상태이고_만료시간_미만인_경우_만료상태_변경_실패() {
		// given
		UUID tokenId = UUID.fromString("01944407-23dc-7806-b923-fe3f1faa5e4a"); //UUID ver 7
		final Long userId = 1L;
		final LocalDateTime currentTime = LocalDateTime.now();

		final QueueToken sut = QueueToken.createWaitingToken(tokenId, userId);
		sut.updateProcessing(currentTime, 5);

		// when
		final boolean expired = sut.expire(currentTime.plusMinutes(3));

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(expired).isFalse();
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.PROCESSING);
		});
	}

	@Test
	@DisplayName("활성 상태이고 만료시간 초과인 경우 만료상태 변경 성공")
	void 활성_상태이고_만료시간_초과인_경우_만료상태_변경_성공() {
		// given
		UUID tokenId = UUID.fromString("01944407-23dc-7806-b923-fe3f1faa5e4a"); //UUID ver 7
		final Long userId = 1L;
		final LocalDateTime currentTime = LocalDateTime.now();

		final QueueToken sut = QueueToken.createWaitingToken(tokenId, userId);
		sut.updateProcessing(currentTime, 5);

		// when
		final boolean expired = sut.expire(currentTime.plusMinutes(6));

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(expired).isTrue();
			softAssertions.assertThat(sut.getStatus()).isEqualTo(QueueTokenStatus.EXPIRED);
		});
	}
}
