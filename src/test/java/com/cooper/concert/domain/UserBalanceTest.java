package com.cooper.concert.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.concert.business.errors.UserErrorCode;
import com.cooper.concert.business.errors.UserErrorType;
import com.cooper.concert.business.errors.exception.InvalidUserPointException;

class UserBalanceTest {

	@Test
	@DisplayName("유저 잔고가 음수인 경우, InvalidUserPointException 반환")
	void 잔고_음수인_경우_생성_실패() {
		// given
		final Long userId = 1L;
		final Long point = -1L;

		// when, then
		assertThatThrownBy(() -> UserBalance.create(userId, point))
			.isInstanceOf(InvalidUserPointException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER03));
	}

	@Test
	@DisplayName("유저 잔고 생성 성공")
	void 잔고_생성_성공() {
		// given
		final Long userId = 1L;
		final Long point = 100L;

		// when
		final UserBalance sut = UserBalance.create(userId, point);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getUserId()).isEqualTo(userId);
			softAssertions.assertThat(sut.getPoint()).isEqualTo(point);
		});
	}

	@Test
	@DisplayName("충전 포인트가 음수인 경우, InvalidUserPointException 반환")
	void 충전_포인트_음수인_경우_잔고_충전_실패() {
		// given
		final Long point = -1L;
		final UserBalance sut = UserBalance.create(1L, 1000L);

		// when, then
		assertThatThrownBy(() -> sut.addPoint(point))
			.isInstanceOf(InvalidUserPointException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode()).isEqualTo(
				UserErrorCode.ERROR_USER06));
	}

	@Test
	@DisplayName("잔고 충전 성공")
	void 잔고_충전_성공() {
		// given
		final Long point = 1000L;
		final UserBalance sut = UserBalance.create(1L, 1000L);

		// when
		final Long updatedBalance = sut.addPoint(point);

		// then
		assertThat(updatedBalance).isEqualTo(2000L);
	}
}
