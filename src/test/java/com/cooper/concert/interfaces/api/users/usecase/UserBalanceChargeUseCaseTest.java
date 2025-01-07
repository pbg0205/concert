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

import com.cooper.concert.domain.users.service.UserBalanceChargeService;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserReadResult;
import com.cooper.concert.interfaces.api.users.usercase.UserBalanceChargeUseCase;

@ExtendWith(MockitoExtension.class)
public class UserBalanceChargeUseCaseTest {

	@InjectMocks
	private UserBalanceChargeUseCase userBalanceChargeUseCase;

	@Mock
	private UserBalanceChargeService userBalanceChargeService;

	@Mock
	private UserReadService userReadService;

	@Test
	@DisplayName("유저 잔고 충전 성공")
	void 유저_잔고_충전_성공() {
		// given
		UUID userAltId = UUID.randomUUID();

		final Long userId = 1L;
		final String userName = "사용자 이름";

		when(userReadService.findByAltId(any())).thenReturn(new UserReadResult(userId, userName));
		when(userBalanceChargeService.chargePoint(any(), any())).thenReturn(new UserBalanceChargeResult(2000L));

		// when
		final UserBalanceChargeResult sut = userBalanceChargeUseCase.chargePoint(userAltId, userId);

		// then
		assertThat(sut.balance()).isEqualTo(2000L);
	}
}
