package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.reservations.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class ConcertQueryRepositoryTest {

	@Autowired
	private ConcertQueryRepository concertQueryRepository;

	@Test
	@DisplayName("콘서트 존재여부 조회 실패")
	@Sql("classpath:sql/repository/concert_repository.sql")
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
	@Sql("classpath:sql/repository/concert_repository.sql")
	void 콘서트_존재여부_조회_성공() {
		// given
		final Long concertId = 1L;

		// when
		final boolean sut = concertQueryRepository.existsById(concertId);

		// then
		assertThat(sut).isTrue();
	}
}
