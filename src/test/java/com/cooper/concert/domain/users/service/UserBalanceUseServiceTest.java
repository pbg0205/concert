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

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserBalanceCommandRepository;

@ExtendWith(MockitoExtension.class)
class UserBalanceUseServiceTest {

	@InjectMocks
	private UserBalanceUseService userBalanceUseService;

	@Mock
	private UserBalanceCommandRepository userBalanceCommandRepository;

	@Test
	@DisplayName("일치하는 유저 키가 없으면 유저 잔고 조회 실패")
	void 일치하는_유저_키가_없으면_유저_잔고_조회_실패() {
		// given
		when(userBalanceCommandRepository.findByUserIdForUpdate(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceUseService.usePoint(1L, 1000L))
			.isInstanceOf(UserBalanceNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat((UserErrorType)errorType).isEqualTo(UserErrorType.USER_BALANCE_NOT_FOUND));
	}

	@Test
	@DisplayName("일치하는 유저 키가 있으면 유저 잔고 조회 성공")
	void 일치하는_유저_키가_있으면_유저_잔고_조회_성공() {
		// given
		final Long userId = 1L;

		when(userBalanceCommandRepository.findByUserIdForUpdate(any())).thenReturn(UserBalance.create(userId, 1000L));

		// when
		final Long balance = userBalanceUseService.usePoint(userId, 1000L);

		// then
		assertThat(balance).isEqualTo(0L);
	}
}
