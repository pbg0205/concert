package com.cooper.concert.domain.queues.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.repository.QueueTokenCommandRepository;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.queues.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class QueueTokenCommandRepositoryTest {

	@Autowired
	private QueueTokenCommandRepository queueTokenCommandRepository;

	@Test
	@DisplayName("활성화 토큰 목록 조회 성공")
	@Sql("classpath:sql/processing_token_repository.sql")
	void 활성화_토큰_목록_조회_성공() {
		// given
		QueueTokenStatus status = QueueTokenStatus.PROCESSING;

		// when
		final List<QueueToken> sut = queueTokenCommandRepository.findAllByStatus(status.name());

		// then
		assertThat(sut).hasSize(2);
	}

}
