package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.QueueTokenQueryRepository;

@ExtendWith(MockitoExtension.class)
class QueueTokenReadServiceTest {

	@InjectMocks
	private QueueTokenReadService queueTokenReadService;

	@Mock
	private QueueTokenQueryRepository queueTokenQueryRepository;

	@Test
	@DisplayName("대기열 토큰이 존재하지 않으면 TokenNotFoundException 반환")
	void 대기열_토큰이_존재하지_않으면_예외_반환() {
		// given
		final UUID tokenId = UUID.fromString("01944a91-0aad-7ff2-9bee-f4802d5aceea");
		when(queueTokenQueryRepository.findUserIdByTokenId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> queueTokenReadService.findUserIdByTokenId(tokenId))
			.isInstanceOf(TokenNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat((TokenErrorType)errorType)
				.isEqualTo(TokenErrorType.TOKEN_NOT_FOUND));
	}

	@Test
	@DisplayName("대기열 토큰이 존재하면 성공")
	void 대기열_토큰이_존재하면_성공() {
		// given
		final UUID tokenId = UUID.fromString("01944a91-0aad-7ff2-9bee-f4802d5aceea");
		when(queueTokenQueryRepository.findUserIdByTokenId(any())).thenReturn(1L);

		// when, then
		assertThat(queueTokenReadService.findUserIdByTokenId(tokenId)).isEqualTo(1L);
	}
}
