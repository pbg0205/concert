package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.queues.models.QueueTokenExpireOutbox;
import com.cooper.concert.domain.queues.models.QueueTokenOutboxStatus;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.queues.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class QueueTokenExpireOutboxRepositoryTest {

	@Autowired
	private QueueTokenExpireOutboxRepository queueTokenExpireOutboxRepository;

	@Test
	@DisplayName("만료 실패 아웃 박스 목록 조회")
	@Sql("classpath:sql/repository/token_expire_fail_list.sql")
	void 만료_실패_객체_목록_조회 () {
	    // given
		final LocalDateTime retryAt = LocalDateTime.of(2030, 12, 31, 2, 0, 0, 0);
		final QueueTokenOutboxStatus status = QueueTokenOutboxStatus.SEND_FAIL;

		// when
		final List<QueueTokenExpireOutbox> sut = queueTokenExpireOutboxRepository.findAllByRetryAtAndType(
			retryAt, status);

		// then
		assertThat(sut).hasSize(3);
	}

}
