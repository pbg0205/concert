package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.repository.WaitingQueueQueryRepository;
import com.cooper.concert.domain.queues.service.dto.response.WaitingTokenPositionInfo;

@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

	@InjectMocks
	private WaitingQueueService waitingQueueService;

	@Mock
	private WaitingQueueQueryRepository waitingQueueQueryRepository;

	@Test
	@DisplayName("토큰 식별자로 대기열 순서 조회 성공")
	void 토큰_식별자로_대기열_순서_조회_성공() {
		// given
		UUID tokenId = UUID.fromString("01944005-6bf3-7d71-9075-509730435585"); // uuid v7

		when(waitingQueueQueryRepository.findTokenPositionByTokenId(any(), any()))
			.thenReturn(3L);

		// when
		final WaitingTokenPositionInfo sut = waitingQueueService.getWaitingTokenPosition(tokenId);

		// then
		assertThat(sut.position()).isEqualTo(3);
	}

	@Test
	@DisplayName("토큰 식별자로 대기열 토큰 존재여부 확인 성공")
	void 토큰_식별자로_대기열_토큰_존재여부_확인_성공() {
		// given
		final Long userId = 7L;

		when(waitingQueueQueryRepository.existsTokenByUserIdAndStatus(any(), any())).thenReturn(true);

		// when
		final boolean sut = waitingQueueService.existQueueToken(userId);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("대기열 토큰 취소 상태 변경 성공")
	void 대기열_토큰_취소_상태_변경_성공() {
		// given
		final UUID tokenId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");
		final Long userId = 1L;

		when(waitingQueueCommandRepository.findByUserIdAndStatus(any(), any()))
			.thenReturn(QueueToken.createWaitingToken(tokenId, userId));

		// when
		final boolean sut = waitingQueueService.updateWaitingToCanceled(userId);

		// then
		assertThat(sut).isTrue();
	}
}
