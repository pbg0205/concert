package com.cooper.concert.domain.queues.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
class WaitingQueueCommandRepositoryTest {

	@Autowired
	private WaitingQueueCommandRepository waitingQueueCommandRepository;

	@Test
	@DisplayName("유저 식별자와 상태를 입력하면 유저 토큰을 반환 성공")
	@Sql("classpath:/sql/repository/waiting_queue_position_repository.sql")
	void 유저_식별자와_상태를_입력하면_유저_토큰_반환_성공() {
		// given
		final Long userId = 1007L;
		final String tokenStatus = QueueTokenStatus.WAITING.name();

		// when
		final QueueToken sut = waitingQueueCommandRepository.findByUserIdAndStatus(userId, tokenStatus);

		// then
		assertSoftly(softAssertions ->{
			softAssertions.assertThat(sut.getUserId()).isEqualTo(userId);
			softAssertions.assertThat(sut.getStatus().name()).isEqualTo(tokenStatus);
		});
	}

	@Test
	@DisplayName("유저 식별자 목록를 입력하면 유저 토큰 목록 조회 성공")
	@Sql("classpath:/sql/repository/accessible_token_list_repository.sql")
	void 유저_식별자_목록를_입력하면_유저_토큰_목록_조회_성공() {
		// given
		final List<Long> ids = List.of(1007L, 1008L, 1009L);

		// when
		final List<QueueToken> sut = waitingQueueCommandRepository.findAllByIds(ids);

		// then
		assertThat(sut).hasSize(3);
	}
}
