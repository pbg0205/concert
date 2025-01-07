package com.cooper.concert.domain.users.service;

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

import com.cooper.concert.domain.users.service.errors.UserErrorCode;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.domain.users.service.repository.UserQueryRepository;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@ExtendWith(MockitoExtension.class)
class UserReadServiceTest {

	@InjectMocks
	private UserReadService userReadService;

	@Mock
	private UserQueryRepository userQueryRepository;

	@Test
	@DisplayName("유저 대체 식별자로 조회하지 못할 경우, UserNotFoundException 반환하며 유저 조회 실패")
	void 대체_식별자_없으면_유저_조회_실패() {
		// given
		final UUID userAltId = UUID.randomUUID();

		when(userQueryRepository.findByAltId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> userReadService.findByAltId(userAltId))
			.isInstanceOf(UserNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((UserErrorType)errorType).getErrorCode())
				.isEqualTo(UserErrorCode.ERROR_USER04));
	}

	@Test
	@DisplayName("유저 포인트 조회 성공")
	void 대체_식별자로_유저_조회_성공() {
		// given
		final UUID userAltId = UUID.randomUUID();
		final Long userId = 1L;
		final String userName = "사용자 이름";

		when(userQueryRepository.findByAltId(any())).thenReturn(new UserReadResult(userId, userName));
		// when
		final UserReadResult sut = userReadService.findByAltId(userAltId);

		// then
		assertThat(sut.userId()).isEqualTo(userId);
	}

}
