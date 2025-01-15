package com.cooper.concert.domain.users.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.users.models.UserBalance;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.users.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class UserBalanceCommandRepositoryTest {

	@Autowired
	private UserBalanceCommandRepository userBalanceCommandRepository;

	@Test
	@DisplayName("유저 존재하지 않을 경우, null 반환")
	@Sql("classpath:sql/repository/user_balance_repository.sql")
	void 유저_미존재_하면_유저_포인트_조회_실패() {
		// given
		final Long userId = 1000L;

		// when
		final UserBalance sut = userBalanceCommandRepository.findByUserIdForUpdate(userId);

		// then
		assertThat(sut).isNull();
	}

	@Test
	@DisplayName("유저 포인트 조회 성공")
	@Sql("classpath:sql/repository/user_balance_repository.sql")
	void 유저_포인트_조회_성공() {
		// given
		final Long userId = 1L;

		// when
		final UserBalance sut = userBalanceCommandRepository.findByUserIdForUpdate(userId);

		// then
		assertThat(sut.getUserId()).isEqualTo(userId);
	}
}
