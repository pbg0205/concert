package com.cooper.concert.interfaces.api.queues.usecase;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.service.QueueTokenIssueService;
import com.cooper.concert.domain.queues.service.WaitingQueueService;
import com.cooper.concert.domain.queues.service.dto.QueueTokenIssueResult;
import com.cooper.concert.domain.queues.service.dto.response.QueueTokenIssueInfo;
import com.cooper.concert.domain.queues.service.dto.response.WaitingTokenPositionInfo;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@ExtendWith(MockitoExtension.class)
class QueueTokenIssueUseCaseTest {

	@InjectMocks
	private QueueTokenIssueUseCase queueTokenIssueUseCase;

	@Mock
	private UserReadService userReadService;

	@Mock
	private QueueTokenIssueService queueTokenIssueService;

	@Mock
	private WaitingQueueService waitingQueueService;

	@Test
	@DisplayName("유저 대체 식별자를 입력하면 대기열 토큰 발급 성공")
	void 유저_대체_식별자를_입력하면_대기열_토큰_발급_성공() {
		// given
		final UUID userAltId = UUID.fromString("0194410c-ff29-781b-8244-c90b25a640f2");
		final UUID tokenId = UUID.fromString("0194410e-fa74-792a-8be0-6a1e7ba32e51");
		final Long position = 3L;

		when(userReadService.findByAltId(any())).thenReturn(new UserReadResult(1L, "사용자 이름"));
		when(queueTokenIssueService.issueQueueToken(any())).thenReturn(new QueueTokenIssueInfo(tokenId));
		when(waitingQueueService.getWaitingTokenPosition(any())).thenReturn(new WaitingTokenPositionInfo(position));

		// when
		final QueueTokenIssueResult sut = queueTokenIssueUseCase.issueQueueToken(userAltId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.tokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.waitingPosition()).isEqualTo(position);
		});
	}

	@Test
	@DisplayName("대기 토큰이 존재하면 기존 토큰 취소하고 재생성")
	void 대기_토큰이_존재하면_기존_토큰_취소() {
		// given
		final UUID userAltId = UUID.fromString("0194410c-ff29-781b-8244-c90b25a640f2");
		final UUID tokenId = UUID.fromString("0194410e-fa74-792a-8be0-6a1e7ba32e51");
		final Long position = 3L;

		when(userReadService.findByAltId(any())).thenReturn(new UserReadResult(1L, "사용자 이름"));
		when(waitingQueueService.existQueueToken(any())).thenReturn(true);
		when(queueTokenIssueService.issueQueueToken(any())).thenReturn(new QueueTokenIssueInfo(tokenId));
		when(waitingQueueService.getWaitingTokenPosition(any())).thenReturn(new WaitingTokenPositionInfo(position));

		// when
		final QueueTokenIssueResult sut = queueTokenIssueUseCase.issueQueueToken(userAltId);

		// then
		assertSoftly(softAssertions -> {
			Mockito.verify(waitingQueueService, Mockito.times(1)).updateWaitingToCanceled(any());
			softAssertions.assertThat(sut.tokenId()).isEqualTo(tokenId);
			softAssertions.assertThat(sut.waitingPosition()).isEqualTo(position);
		});
	}

}
