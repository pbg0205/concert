package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.QueueTokenQueryRepository;

@ExtendWith(MockitoExtension.class)
class QueueTokenValidationServiceTest {

	@InjectMocks
	private QueueTokenValidationService queueTokenValidationService;

	@Mock
	private QueueTokenQueryRepository queueTokenQueryRepository;

	@Test
	@DisplayName("토큰 조회에 실패하면 검증 실패")
	void 토큰_조회에_실패하면_검증_실패() {
		// given
		final UUID tokenId = UUID.fromString("0194466b-33bb-7f5f-8db4-c2c2c988bb70"); // uuid ver7

		when(queueTokenQueryRepository.findByTokenId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(
			() -> queueTokenValidationService.validateQueueTokenProcessing(tokenId, LocalDateTime.now().plusMinutes(5)))
			.isInstanceOf(TokenNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat((TokenErrorType)errorType).isEqualTo(TokenErrorType.TOKEN_NOT_FOUND));
	}

	@Test
	@DisplayName("조회한_토큰이_만료시간_초과한_경우_비활성화_토큰")
	void 조회한_토큰이_만료시간_초과한_경우_비활성화_토큰() {
		// given
		final UUID tokenId = UUID.fromString("0194466b-33bb-7f5f-8db4-c2c2c988bb70"); // uuid ver7
		final QueueToken token = QueueToken.createWaitingToken(tokenId, 1L);

		when(queueTokenQueryRepository.findByTokenId(any())).thenReturn(token);
		token.updateProcessing(LocalDateTime.of(2025, 1, 9, 0, 20), 5);

		// when
		final boolean tokenProcessingValid =
			queueTokenValidationService.validateQueueTokenProcessing(tokenId, LocalDateTime.of(2025, 1, 9, 0, 26));

		// then
		assertThat(tokenProcessingValid).isFalse();
	}

	@Test
	@DisplayName("조회한 토큰이 만료시간 이내 경우 활성화 토큰")
	void 조회한_토큰이_만료시간_이내_경우_활성화_토큰() {
		// given
		final UUID tokenId = UUID.fromString("0194466b-33bb-7f5f-8db4-c2c2c988bb70"); // uuid ver7
		final QueueToken token = QueueToken.createWaitingToken(tokenId, 1L);

		when(queueTokenQueryRepository.findByTokenId(any())).thenReturn(token);
		token.updateProcessing(LocalDateTime.of(2025, 1, 9, 0, 20), 5);

		// when
		final boolean tokenProcessingValid =
			queueTokenValidationService.validateQueueTokenProcessing(tokenId, LocalDateTime.of(2025, 1, 9, 0, 22));

		// then
		assertThat(tokenProcessingValid).isTrue();
	}
}
