package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.repository.QueueTokenCommandRepository;

@ExtendWith(MockitoExtension.class)
class QueueTokenExpiredServiceTest {

	@InjectMocks
	private QueueTokenExpiredService queueTokenExpiredService;

	@Mock
	private QueueTokenCommandRepository queueTokenCommandRepository;

	@ParameterizedTest
	@MethodSource("queueTokenSource")
	@DisplayName("만료 예정 토큰 목록이 없으면 토큰 만료 없음")
	void 만료_예정_토큰_목록이_없으면_토큰_만료_없음(List<QueueToken> queueTokens) {
		// given
		final LocalDateTime currentTime = LocalDateTime.of(2025, 1, 8, 14, 0);
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 8, 14, 3);

		for (QueueToken queueToken : queueTokens) {
			queueToken.updateProcessing(currentTime, 5);
		}

		when(queueTokenCommandRepository.findAllByStatus(QueueTokenStatus.PROCESSING.name()))
			.thenReturn(queueTokens);

		// when
		final Integer sut = queueTokenExpiredService.updateToExpired(expiredAt);

		// then
		assertThat(sut).isEqualTo(0);
	}

	@ParameterizedTest
	@MethodSource("queueTokenSource")
	@DisplayName("만료 예정 토큰이 존재하면 토큰 만료 성공")
	void 만료_예정_토큰이_존재하면_토큰_만료_성공(List<QueueToken> queueTokens) {
		// given
		final LocalDateTime currentTime = LocalDateTime.of(2025, 1, 8, 14, 0);
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 8, 14, 6);

		for (QueueToken queueToken : queueTokens) {
			queueToken.updateProcessing(currentTime, 5);
		}

		when(queueTokenCommandRepository.findAllByStatus(QueueTokenStatus.PROCESSING.name()))
			.thenReturn(queueTokens);

		// when
		final Integer sut = queueTokenExpiredService.updateToExpired(expiredAt);

		// then
		assertThat(sut).isEqualTo(5);
	}

	private static Stream<Arguments> queueTokenSource() {
		return Stream.of(
			Arguments.arguments(List.of(
				QueueToken.createWaitingToken(UUID.randomUUID(), 1L),
				QueueToken.createWaitingToken(UUID.randomUUID(), 2L),
				QueueToken.createWaitingToken(UUID.randomUUID(), 3L),
				QueueToken.createWaitingToken(UUID.randomUUID(), 4L),
				QueueToken.createWaitingToken(UUID.randomUUID(), 5L)
			))
		);
	}
}
