package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.queues.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
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
