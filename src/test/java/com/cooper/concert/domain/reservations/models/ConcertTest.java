package com.cooper.concert.domain.reservations.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertCreationException;

class ConcertTest {

	@Test
	@DisplayName("콘서트 이름이 빈값이면 콘서트 생성 실패")
	void 콘서트_이름이_빈값이면_콘서트_생성_실패() {
		// given
		final String name = "";

		// when, then
		assertThatThrownBy(() -> Concert.create(name))
			.isInstanceOf(ConcertCreationException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((ConcertErrorType)errorType)).isEqualTo(
				ConcertErrorType.CONCERT_NAME_EMPTY));
	}

	@Test
	@DisplayName("콘서트 이름이 존재하면 콘서트 생성 성공")
	void 콘서트_이름이_존재하면_콘서트_생성_성공() {
		// given
		final String name = "콘서트 이름";

		// when
		final Concert sut = Concert.create(name);

		// then
		assertThat(sut).extracting("name").isEqualTo(name);
	}

}
