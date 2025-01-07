package com.cooper.concert.interfaces.api.users.usecase;

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

import com.cooper.concert.domain.users.service.UserBalanceReadService;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;
import com.cooper.concert.domain.users.service.response.UserReadResult;
import com.cooper.concert.interfaces.api.users.usercase.UserBalanceReadUseCase;

@ExtendWith(MockitoExtension.class)
class UserBalanceReadUseCaseTest {

	@InjectMocks
	private UserBalanceReadUseCase userBalanceReadUseCase;

	@Mock
	private UserBalanceReadService userBalanceReadService;

	@Mock
	private UserReadService userReadService;

	@Test
	@DisplayName("유저 잔고 조회 성공")
	void 유저_잔고_조회_성공() {
		// given
		UUID userAltId = UUID.randomUUID();

		final Long userId = 1L;
		final String userName = "사용자 이름";

		when(userReadService.findByAltId(any())).thenReturn(new UserReadResult(userId, userName));
		when(userBalanceReadUseCase.readUserBalance(any())).thenReturn(new UserBalanceReadResult(1000L));

		// when
		final UserBalanceReadResult sut = userBalanceReadUseCase.readUserBalance(userAltId);

		// then
		assertThat(sut.balance()).isEqualTo(1000L);
	}
}
