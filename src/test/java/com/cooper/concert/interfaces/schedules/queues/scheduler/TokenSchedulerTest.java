package com.cooper.concert.interfaces.schedules.queues.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.interfaces.schedules.queues.usecase.TokenSchedulerUseCase;

@SpringBootTest
class TokenSchedulerTest {

	@Autowired
	private TokenSchedulerUseCase tokenSchedulerUseCase;

	@Test
	@DisplayName("토큰 활성화 상태 변경 성공")
	@Sql("classpath:sql/token_update_processing_integration.sql")
	void 토큰_활성화_상태_변경_성공() {
		// given
		final LocalDateTime expiredAt = LocalDateTime.of(2025, 1, 7, 11, 59);

		// when
		final Integer successCount = tokenSchedulerUseCase.updateToProcessing(expiredAt);

		// then
		assertThat(successCount).isEqualTo(4);
	}
}
