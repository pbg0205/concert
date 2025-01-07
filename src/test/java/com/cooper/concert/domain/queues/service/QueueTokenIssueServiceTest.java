package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cooper.concert.domain.queues.generator.TokenIdGenerator;
import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.repository.QueueTokenCommandRepository;
import com.cooper.concert.domain.queues.service.dto.response.QueueTokenIssueInfo;

class QueueTokenIssueServiceTest {

	private QueueTokenIssueService queueTokenIssueService;

	private TokenIdGenerator tokenIdGenerator;

	private QueueTokenCommandRepository queueTokenCommandRepository;

	@BeforeEach
	void setUp() {
		this.tokenIdGenerator = new TokenIdGenerator();
		this.queueTokenCommandRepository = Mockito.mock(QueueTokenCommandRepository.class);
		this.queueTokenIssueService = new QueueTokenIssueService(tokenIdGenerator, queueTokenCommandRepository);
	}

	@Test
	@DisplayName("대기열 토큰 발급 성공")
	void 대기열_토큰_발급_성공() {
		// given
		final UUID tokenId = tokenIdGenerator.generateTokenId();
		final Long userId = 1L;

		when(queueTokenCommandRepository.save(any())).thenReturn(QueueToken.createWaitingToken(tokenId, userId));

		// when
		final QueueTokenIssueInfo sut = queueTokenIssueService.issueQueueToken(userId);

		// then
		assertThat(sut.tokenId()).isEqualTo(tokenId);
	}

}
