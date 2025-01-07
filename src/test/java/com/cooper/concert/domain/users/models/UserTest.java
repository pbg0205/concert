package com.cooper.concert.domain.users.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.concert.domain.users.service.errors.UserErrorCode;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.InvalidUserNameException;

class UserTest {

	@Test
	@DisplayName("유저 이름이 빈 값인 경우, InvalidUserNameException 반환")
	void 유저_이름_길이_빈_값인_경우_생성_실패() {
		// given
		final String name = "";

		// when, then
		assertThatThrownBy(() -> User.create("", UUID.randomUUID()))
			.isInstanceOf(InvalidUserNameException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER01));
	}

	@Test
	@DisplayName("유저 이름 길이가 100 초과인 경우, InvalidUserNameException 반환")
	void 유저_이름_100_초과_경우_생성_실패() {
		// given
		final String name = "_0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

		// when, then
		assertThatThrownBy(() -> User.create(name, UUID.randomUUID()))
			.isInstanceOf(InvalidUserNameException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER02));
	}

	@Test
	@DisplayName("유저 생성 성공")
	void 유저__생성_성공() {
		// given
		final UUID altId = UUID.fromString("01943efe-ba03-7424-bba5-79c797d0a499"); // uuid v7;
		final String name = "cooper";

		// when
		final User sut = User.create(name, altId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.getAltId()).isEqualTo(altId);
			softAssertions.assertThat(sut.getName()).isEqualTo(name);
		});
	}
}
