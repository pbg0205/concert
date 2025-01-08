package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;

@DataJpaTest
@ComponentScan(basePackages = {"com.cooper.concert.domain.reservations.infrastructure.rdb",  "com.cooper.concert.common.jpa"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class ConcertQueryRepositoryTest {

	@Autowired
	private ConcertQueryRepository concertQueryRepository;

	@Test
	@DisplayName("콘서트 존재여부 조회 실패")
	@Sql("classpath:sql/concert.sql")
	void 콘서트_존재여부_조회_실패() {
		// given
		final Long concertId = 4L;

		// when
		final boolean sut = concertQueryRepository.existsById(concertId);

		// then
		assertThat(sut).isFalse();
	}


	@Test
	@DisplayName("콘서트 존재여부 조회 성공")
	@Sql("classpath:sql/concert.sql")
	void 콘서트_존재여부_조회_성공() {
		// given
		final Long concertId = 1L;

		// when
		final boolean sut = concertQueryRepository.existsById(concertId);

		// then
		assertThat(sut).isTrue();
	}
}
