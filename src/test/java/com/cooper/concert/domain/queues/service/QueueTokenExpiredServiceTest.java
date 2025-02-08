package com.cooper.concert.domain.queues.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@ExtendWith(MockitoExtension.class)
class QueueTokenExpiredServiceTest {

	@InjectMocks
	private QueueTokenExpiredService queueTokenExpiredService;

	@Mock
	private ActiveTokenRepository activeTokenRepository;

	@Test
	@DisplayName("만료 예정 토큰 목록이 없으면 토큰 만료 없음")
	void 만료_예정_토큰_목록이_없으면_토큰_만료_없음() {
		// given
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 8, 14, 3);

		when(activeTokenRepository.removeAllLteExpiredAt(any())).thenReturn(List.of());

		// when
		final List<Long> sut = queueTokenExpiredService.expireExpiredTokens(expiredAt);

		// then
		assertThat(sut).hasSize(0);
	}

	@Test
	@DisplayName("만료 예정 토큰이 존재하면 토큰 만료 성공")
	void 만료_예정_토큰이_존재하면_토큰_만료_성공() {
		// given
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 8, 14, 6);

		when(activeTokenRepository.removeAllLteExpiredAt(any())).thenReturn(List.of(1L, 2L, 3L));

		// when
		final List<Long> sut = queueTokenExpiredService.expireExpiredTokens(expiredAt);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).hasSize(3);
			softAssertions.assertThat(sut).containsAll(List.of(1L, 2L, 3L));
		});
	}

	@Test
	@DisplayName("예약 완료 토큰을 찾지 못하는 경우 예외 반환")
	void 예약_완료_토큰을_찾지_못하는_경우_예외_반환() {
		// given
		when(activeTokenRepository.removeActiveToken(any())).thenReturn(false);

		// when. then
		assertThatThrownBy(() -> queueTokenExpiredService.expireCompleteToken(1L))
			.isInstanceOf(TokenNotFoundException.class)
			.extracting("errorType").isEqualTo(TokenErrorType.TOKEN_NOT_FOUND);
	}

}
