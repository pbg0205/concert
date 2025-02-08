package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@ExtendWith(MockitoExtension.class)
class ActiveQueueTokenValidationServiceTest {

	@InjectMocks
	private ActiveQueueTokenValidationService activeQueueTokenValidationService;

	@Mock
	private QueueTokenExtractor queueTokenExtractor;

	@Mock
	private ActiveTokenRepository activeTokenRepository;

	@Test
	@DisplayName("활성화 토큰 검증 성공")
	void 활성화_토큰_검증_성공 () {
	    // given
		when(queueTokenExtractor.extractUserIdFromToken(any())).thenReturn(1L);
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

	    // when
		final boolean sut = activeQueueTokenValidationService.validateActiveQueueToken("token");

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("활성화 토큰 목록에서 조회 실패하면 검증 실패")
	void 활성화_토큰_목록에서_조회_실패하면_검증_실패 () {
	    // given
		when(queueTokenExtractor.extractUserIdFromToken(any())).thenReturn(1L);
		when(activeTokenRepository.existsByUserId(any())).thenReturn(false);

	    // when
		final boolean sut = activeQueueTokenValidationService.validateActiveQueueToken("token");

		// then
		assertThat(sut).isFalse();
	}

}
