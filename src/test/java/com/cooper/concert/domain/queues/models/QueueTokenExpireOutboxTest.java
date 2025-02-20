package com.cooper.concert.domain.queues.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueueTokenExpireOutboxTest {

	@Test
	@DisplayName("토큰 만료 아웃박스 생성")
	void 토큰_만료_아웃박스_생성 () {
	    // given, when
		final QueueTokenExpireOutbox queueTokenExpireOutbox = QueueTokenExpireOutbox.createInitStatus("topic",
			"payloadJson", UUID.randomUUID());

		// then
		assertThat(queueTokenExpireOutbox).isNotNull();
	}
	
	@Test
	@DisplayName("토큰 만료 아웃박스 전송 상태 변경 성공")
	void 토큰_만료_아웃박스_전송_상태_변경_성공 () {
	    // given
		final QueueTokenExpireOutbox queueTokenExpireOutbox = QueueTokenExpireOutbox.createInitStatus("topic",
			"payloadJson", UUID.randomUUID());

		// when
		final boolean updateSuccess = queueTokenExpireOutbox.updateSuccess();

		// then
		assertThat(updateSuccess).isTrue();
	}

	@Test
	@DisplayName("토큰 만료 아웃박스 전송 실패 상태 변경 성공")
	void 토큰_만료_아웃박스_전송_실패_상태_변경_성공 () {
	    // given
		final QueueTokenExpireOutbox queueTokenExpireOutbox = QueueTokenExpireOutbox.createInitStatus("topic",
			"payloadJson", UUID.randomUUID());

		// when
		final boolean updateFail = queueTokenExpireOutbox.updateFail();

		// then
		assertThat(updateFail).isTrue();
	}

}
