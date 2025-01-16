package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.queues.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class WaitingQueueQueryRepositoryTest {

	@Autowired
	private WaitingQueueQueryRepository waitingQueueQueryRepository;

	@Test
	@DisplayName("대기열 순서 조회 성공")
	@Sql("classpath:sql/repository/waiting_queue_position_repository.sql")
	void 대기열_순서_조회_성공() {
		// given
		final UUID tokenId = UUID.fromString("01b8f8a1-6f8c-7b6e-87c3-234a3c15f804");

		// when
		final Long sut = waitingQueueQueryRepository.findTokenPositionByTokenId(tokenId, "WAITING");

		// then
		assertThat(sut).isEqualTo(4);
	}

	@Test
	@DisplayName("사용 가능한 대기 토큰 존재여부 성공")
	@Sql("classpath:sql/repository/waiting_queue_position_repository.sql")
	void 사용_가능한_대기_토큰_존재여부_성공() {
		// given
		final Long userId = 1007L; // WAITING 대기열 토믄
		final String tokenStatus = QueueTokenStatus.WAITING.name();

		// when
		final boolean sut = waitingQueueQueryRepository.existsTokenByUserIdAndStatus(userId, tokenStatus);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("대기열 토큰 활성화 상태 사용자 수 조회 성공")
	@Sql("classpath:/sql/repository/processing_token_counts_repository.sql")
	void 대기열_토큰_활성화_상태_사용자_수_조회_성공() {
		// given
		final LocalDateTime expiredAt = Instant.ofEpochMilli(1736251200000L)
			.atOffset(ZoneOffset.UTC)
			.toLocalDateTime(); // 2025.01.07T12:00:00

		// when
		final Integer sut = waitingQueueQueryRepository.countsTokenByStatusAndExpiredAt(
			QueueTokenStatus.PROCESSING.name(), expiredAt);

		// then
		assertThat(sut).isEqualTo(3);
	}

	@Test
	@DisplayName("일정 수, 특정 토큰 상태 아이디 목록 조회 성공")
	@Sql("classpath:sql/repository/waiting_queue_ids_limit_position_repository.sql")
	void 일정_수_특정_토큰_상태_아이디_목록_조회_성공() {
		// given, when
		final List<Long> sut = waitingQueueQueryRepository.findAccessibleIdsByStatusOrderByIdAsc(
			QueueTokenStatus.WAITING.name(), 3);

		// then
		assertThat(sut).containsAll(List.of(1007L, 1008L, 1009L));
	}
}
