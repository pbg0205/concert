package com.cooper.concert.domain.reservations.infrastructure.cache;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleCacheRepository;

@Repository
@RequiredArgsConstructor
public class RedisConcertScheduleCacheRepository implements ConcertScheduleCacheRepository {

	@Value("${concert.schedules.cache.format}")
	private String concertScheduleKeyFormat;

	@Value("${concert.schedules.cache.ttl}")
	private Long concertScheduleTTL;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public ConcertScheduleResult findById(final Long concertId, final Long concertScheduleId) {
		final String concertScheduleKey = String.format(concertScheduleKeyFormat, concertId);

		if (Boolean.FALSE.equals(redisTemplate.hasKey(concertScheduleKey))) {
			throw new ConcertNotFoundException(ConcertErrorType.CONCERT_NOT_FOUND);
		}

		return Optional.ofNullable(
				(ConcertScheduleResult)redisTemplate.opsForHash().get(concertScheduleKey, concertScheduleId))
			.orElseThrow(() -> new ConcertScheduleNotFoundException(ConcertErrorType.CONCERT_SCHEDULE_NOT_FOUND));
	}

	@Override
	public void saveConcertSchedule(final Long concertId, final ConcertScheduleResult concertScheduleResult) {
		final String concertScheduleKey = String.format(concertScheduleKeyFormat, concertId);

		redisTemplate.opsForHash()
			.put(concertScheduleKey, concertScheduleResult.concertScheduleId(), concertScheduleResult);

		redisTemplate.expire(concertScheduleKey, Duration.of(concertScheduleTTL, ChronoUnit.SECONDS));
	}

}
