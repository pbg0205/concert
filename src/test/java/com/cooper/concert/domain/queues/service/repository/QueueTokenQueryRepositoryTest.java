package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.queues.models.QueueToken;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.queues.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
}
