package com.cooper.concert.domain.users.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.users.models.UserBalance;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.users.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserBalanceCommandRepositoryTest {

	@Autowired
	private UserBalanceCommandRepository userBalanceCommandRepository;

	@Test
	@DisplayName("유저 존재하지 않을 경우, null 반환")
	@Sql("classpath:sql/user_balance_repository.sql")
	void 유저_미존재_하면_유저_포인트_조회_실패() {
		// given
		final Long userId = 1000L;

		// when
		final UserBalance sut = userBalanceCommandRepository.findByUserId(userId);

		// then
		assertThat(sut).isNull();
	}

	@Test
	@DisplayName("유저 포인트 조회 성공")
	@Sql("classpath:sql/user_balance_repository.sql")
	void 유저_포인트_조회_성공() {
		// given
		final Long userId = 1L;

		// when
		final UserBalance sut = userBalanceCommandRepository.findByUserId(userId);

		// then
		assertThat(sut.getUserId()).isEqualTo(userId);
	}
}