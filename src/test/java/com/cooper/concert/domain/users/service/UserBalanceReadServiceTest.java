package com.cooper.concert.domain.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.users.repository.UserBalanceQueryRepository;
import com.cooper.concert.domain.users.service.errors.UserErrorCode;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;

@ExtendWith(MockitoExtension.class)
class UserBalanceReadServiceTest {

	@InjectMocks
	private UserBalanceReadService userBalanceReadService;

	@Mock
	private UserBalanceQueryRepository userBalanceQueryRepository;

	@Test
	@DisplayName("유저 식별자로 조회하지 못할 경우, UserBalanceNotFoundException 반환하며 포인트 조회 실패")
	void 유저_식별자로_조회_실패하면_포인트_조회_실패() {
		// given
		final Long userId = 1L;

		when(userBalanceQueryRepository.findByUserId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceReadService.readUserBalance(userId))
			.isInstanceOf(UserBalanceNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER05));
	}

	@Test
	@DisplayName("유저 식별자로 유저 포인트 조회 성공")
	void 유저_식별자로_유저_포인트_조회_성공() {
		// given
		final Long userId = 1L;
		final String userName = "사용자 이름";

		when(userBalanceQueryRepository.findByUserId(any())).thenReturn(new UserBalanceReadResult(1000L));

		// when
		final UserBalanceReadResult sut = userBalanceReadService.readUserBalance(userId);

		// then
		assertThat(sut.balance()).isEqualTo(1000L);
	}

}
