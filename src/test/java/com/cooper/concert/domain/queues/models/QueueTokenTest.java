package com.cooper.concert.domain.queues.models;

import static org.assertj.core.api.Assertions.assertThat;
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

	@Test
	@DisplayName("상태는 활성화지만 만료 시간을 초과한 경우 비활성화 토큰")
	void 상태는_활성화지만_만료_시간을_초과한_경우_비활성화_토큰 () {
		// given
		final QueueToken sut = QueueToken.createWaitingToken(UUID.randomUUID(), 1L);

		sut.updateProcessing(LocalDateTime.of(2025, 1, 8, 11, 50), 5);

		// when
		final boolean validProcessing = sut.isValidProcessingToken(LocalDateTime.of(2025, 1, 8, 11, 58));

		// then
		assertThat(validProcessing).isFalse();
	}

	@Test
	@DisplayName("상태가 활성화가 아닌 경우 비활성화 토큰")
	void 상태가_활성화가_아닌_경우_비활성화_토큰 () {
		// given
		final UUID tokenId = UUID.fromString("0194466b-33bb-7f5f-8db4-c2c2c988bb70"); // uuid ver7

		final QueueToken sut = QueueToken.createWaitingToken(tokenId, 1L);

		// when
		final boolean validProcessing = sut.isValidProcessingToken(LocalDateTime.of(2025, 1, 8, 11, 58));

		// then
		assertThat(validProcessing).isFalse();
	}


	@Test
	@DisplayName("상태는 활성화이고 만료 시간 이내인 경우 활성화 토큰")
	void 상태는_활성화이고_만료_시간_이내인_경우_활성화_토큰 () {
		// given
		final QueueToken sut = QueueToken.createWaitingToken(UUID.randomUUID(), 1L);

		sut.updateProcessing(LocalDateTime.of(2025, 1, 8, 11, 50), 5);

		// when
		final boolean validProcessing = sut.isValidProcessingToken(LocalDateTime.of(2025, 1, 8, 11, 52));

		// then
		assertThat(validProcessing).isTrue();
	}

	@Test
	@DisplayName("예약 완료된 토큰 상태와 만료 시간 변경")
	void 예약_완료된_토큰_상태와_만료_시간_변경 () {
	    // given
		final LocalDateTime expiredAt = LocalDateTime.now();
		QueueToken sut = QueueToken.createWaitingToken(UUID.randomUUID(), 1L);
		sut.updateProcessing(LocalDateTime.now().plusMinutes(5), 5);

	    // when
		final boolean completed = sut.complete(expiredAt);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(completed).isTrue();
			softAssertions.assertThat(sut).extracting("status").isEqualTo(QueueTokenStatus.COMPLETED);
			softAssertions.assertThat(sut).extracting("expiredAt").isEqualTo(expiredAt);
		});
	}

}
