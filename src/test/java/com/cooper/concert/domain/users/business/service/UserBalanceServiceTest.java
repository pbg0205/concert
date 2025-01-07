package com.cooper.concert.domain.users.business.service;

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

import com.cooper.concert.domain.users.repository.UserBalanceRepository;
import com.cooper.concert.domain.users.repository.UserRepository;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;
import com.cooper.concert.domain.users.service.errors.UserErrorCode;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserBalanceNotFoundException;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.domain.users.models.User;
import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.UserBalanceService;

@ExtendWith(MockitoExtension.class)
class UserBalanceServiceTest {

	@InjectMocks
	private UserBalanceService userBalanceService;

	@Mock
	private UserBalanceRepository userBalanceRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("유저를 조회하지 못할 경우, UserNotFoundException 반환하며 포인트 충전 실패")
	void 유저_조회_실패하면_포인트_충전_실패() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userRepository.findByAltId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceService.chargePoint(userAltId, 1000L))
			.isInstanceOf(UserNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER04));
	}

	@Test
	@DisplayName("유저 잔고 조회하지 못할 경우, UserBalanceNotFoundException 반환하며 포인트 충전 실패")
	void 유저_잔고_조회_실패하면_포인트_충전_실패() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userRepository.findByAltId(any())).thenReturn(User.create("사용자1", userAltId));
		when(userBalanceRepository.findByUserId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceService.chargePoint(userAltId, 1000L))
			.isInstanceOf(UserBalanceNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER05));
	}

	@Test
	@DisplayName("유저 포인트 충전 성공")
	void 유저_포인트_충전_성공() {
		// given
		final UUID userAltId = UUID.randomUUID();
		final Long point = 1000L;

		when(userRepository.findByAltId(any())).thenReturn(User.create("사용자1", userAltId));
		when(userBalanceRepository.findByUserId(any())).thenReturn(UserBalance.create(1L, 1000L));

		// when
		final UserBalanceChargeResult sut = userBalanceService.chargePoint(userAltId, point);

		// then
		assertThat(sut.balance()).isEqualTo(2000L);
	}

	@Test
	@DisplayName("유저를 조회하지 못할 경우, UserNotFoundException 반환하며 포인트 조회 실패")
	void 유저_조회_실패하면_포인트_조회_실패() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userRepository.findByAltId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceService.readUserBalance(userAltId))
			.isInstanceOf(UserNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER04));
	}

	@Test
	@DisplayName("유저 잔고 조회하지 못할 경우, UserBalanceNotFoundException 반환하며 포인트 조회 실패")
	void 유저_잔고_조회_실패하면_포인트_조회_실패() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userRepository.findByAltId(any())).thenReturn(User.create("사용자1", userAltId));
		when(userBalanceRepository.findByUserId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userBalanceService.chargePoint(userAltId, 1000L))
			.isInstanceOf(UserBalanceNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER05));
	}

	@Test
	@DisplayName("유저 포인트 조회 성공")
	void 유저_포인트_조회_성공() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userRepository.findByAltId(any())).thenReturn(User.create("사용자1", userAltId));
		when(userBalanceRepository.findByUserId(any())).thenReturn(UserBalance.create(1L, 1000L));

		// when
		final UserBalanceReadResult sut = userBalanceService.readUserBalance(userAltId);

		// then
		assertThat(sut.balance()).isEqualTo(1000L);
	}
}
