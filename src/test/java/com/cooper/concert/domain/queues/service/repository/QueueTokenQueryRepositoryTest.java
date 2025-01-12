package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.queues.models.QueueToken;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.queues.infrastructure.rdb",
	"com.cooper.concert.common.jpa"})
class QueueTokenQueryRepositoryTest {

	@Autowired
	private QueueTokenQueryRepository queueTokenQueryRepository;

	@Test
	@DisplayName("토큰 아이디로 대기열 토큰 조회 실패")
	@Sql("classpath:sql/token_read_repository.sql")
	void 토큰_아이디로_대기열_토큰_조회_실패() {
		// given
		final UUID tokenId = UUID.fromString("0194467c-fb8e-7706-af9e-b855b7860fea");

		// when
		final QueueToken sut = queueTokenQueryRepository.findByTokenId(tokenId);

		// then
		assertThat(sut).isNull();
	}

	@Test
	@DisplayName("토큰 아이디로 대기열 토큰 조회 성공")
	@Sql("classpath:sql/token_read_repository.sql")
	void 토큰_아이디로_대기열_토큰_조회_성공() {
		// given
		final UUID tokenId = UUID.fromString("01b8f8a1-6f8c-7b6e-87c3-234a3c15f78f");

		// when
		final QueueToken sut = queueTokenQueryRepository.findByTokenId(tokenId);

		// then
		assertThat(sut).isNotNull();
	}

	@Test
	@DisplayName("토큰 아이디로 대기열 토큰 조회 성공")
	@Sql("classpath:sql/token_read_repository.sql")
	void 토큰_아이디로_유저_아이디_조회_성공() {
		// given
		final UUID tokenId = UUID.fromString("01b8f8a1-6f8c-7b6e-87c3-234a3c15f78f");

		// when
		final Long sut = queueTokenQueryRepository.findUserIdByTokenId(tokenId);

		// then
		assertThat(sut).isEqualTo(1002);
	}

}
