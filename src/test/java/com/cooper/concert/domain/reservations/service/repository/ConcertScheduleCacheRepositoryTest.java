package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleNotFoundException;

@SpringBootTest
class ConcertScheduleCacheRepositoryTest {

	@Value("${concert.schedules.cache.format}")
	private String concertScheduleKeyFormat;

	@Autowired
	private ConcertScheduleCacheRepository concertScheduleCacheRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void tearDown() {
		final Set<String> keys = redisTemplate.keys("*");
		redisTemplate.delete(keys);
	}

	@Test
	@DisplayName("콘서트 스케줄 목록 저장 성공")
	void 콘서트_스케줄_목록_조회() {
		// given
		final Long concertId = 1L;
		final String concertScheduleKey = String.format(concertScheduleKeyFormat, concertId);

		// when
		concertScheduleCacheRepository.saveConcertSchedule(concertId,
			new ConcertScheduleResult(1L, LocalDateTime.of(2025, 2, 1, 17, 0)));

		// then
		final Set<Object> sut = redisTemplate.opsForHash().keys(concertScheduleKey);

		assertThat(sut).hasSize(1);
	}

	@Test
	@DisplayName("콘서트 스케줄 단일 조회 성공")
	void 콘서트_스케줄_단일_조회_성공() {
		// given
		final Long concertId = 1L;
		final String concertScheduleKey = String.format(concertScheduleKeyFormat, concertId);

		final Long concertScheduleId = 2L;

		redisTemplate.opsForHash().put(concertScheduleKey, 2L,
			new ConcertScheduleResult(concertScheduleId, LocalDateTime.of(2025, 2, 9, 18, 0)));

		// when
		final ConcertScheduleResult sut = concertScheduleCacheRepository.findById(concertId, concertScheduleId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).isNotNull();
			softAssertions.assertThat(sut).extracting("concertScheduleId").isEqualTo(concertScheduleId);
		});
	}

	@Test
	@DisplayName("콘서트가 존재하지 않으면 단일 조회 실패")
	void 콘서트가_존재하지_않으면_단일_조회_실패() {
		// given
		final Long concertId = 1L;
		final Long concertScheduleId = 2L;

		// when
		assertThatThrownBy(() -> concertScheduleCacheRepository.findById(concertId, concertScheduleId))
			.isInstanceOf(ConcertNotFoundException.class)
			.extracting("errorType").isEqualTo(ConcertErrorType.CONCERT_NOT_FOUND);
	}

	@Test
	@DisplayName("콘서트 스케줄이 존재하지 않으면 단일 조회 실패")
	void 콘서트_스케줄이_존재하지_않으면_단일_조회_실패() {
		// given
		final Long concertId = 1L;
		final String concertScheduleKey = String.format(concertScheduleKeyFormat, concertId);

		final Long concertScheduleId = 2L;

		redisTemplate.opsForHash().put(concertScheduleKey, 1L,
			new ConcertScheduleResult(concertScheduleId, LocalDateTime.of(2025, 2, 9, 18, 0)));

		// when
		assertThatThrownBy(() -> concertScheduleCacheRepository.findById(concertId, concertScheduleId))
			.isInstanceOf(ConcertScheduleNotFoundException.class)
			.extracting("errorType").isEqualTo(ConcertErrorType.CONCERT_SCHEDULE_NOT_FOUND);
	}

}
