package com.cooper.concert.domain.users.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.users.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class UserQueryRepositoryTest {

	@Autowired
	private UserQueryRepository userQueryRepository;

	@Test
	@DisplayName("유저 존재하지 않을 경우, null 반환")
	@Sql("classpath:sql/repository/user_repository.sql")
	void 유저_미존재_하면_유저_조회_실패() {
		// given
		final UUID userAltId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e29999");

		// when
		final UserReadResult sut = userQueryRepository.findByAltId(userAltId);

		// then
		assertThat(sut).isNull();
	}

	@Test
	@DisplayName("유저 조회 성공")
	@Sql("classpath:sql/repository/user_repository.sql")
	void 유저_조회_성공() {
		// given
		final UUID userAltId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");

		// when
		final UserReadResult sut = userQueryRepository.findByAltId(userAltId);

		// then
		assertThat(sut.userId()).isEqualTo(1L);
	}
}
