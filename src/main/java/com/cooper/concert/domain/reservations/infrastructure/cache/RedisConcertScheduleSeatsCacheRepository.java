package com.cooper.concert.domain.reservations.infrastructure.cache;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.request.ConcertSeatOccupyCancelRequest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertSeatNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleSeatsCacheRepository;

@Repository
@RequiredArgsConstructor
public class RedisConcertScheduleSeatsCacheRepository implements ConcertScheduleSeatsCacheRepository {

	@Value("${concert.schedules.available-seats.cache.format}")
	private String concertSeatsKeyFormat;

	@Value("${concert.schedules.available-seats.cache.ttl}")
	private Long concertScheduleSeatTTL;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public boolean existsByConcertScheduleId(final Long concertScheduleId) {
		final String concertSeatsKey = String.format(concertSeatsKeyFormat, concertScheduleId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(concertSeatsKey));
	}

	@Override
	public List<ConcertSeatResult> findAvailableSeatsByScheduleId(final Long concertScheduleId) {
		final String concertSeatsKey = String.format(concertSeatsKeyFormat, concertScheduleId);
		final List<Object> concertSeats = redisTemplate.opsForList().range(concertSeatsKey, 0, -1);

		if (concertSeats == null || concertSeats.isEmpty()) {
			throw new ConcertSeatNotFoundException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND);
		}

		return Objects.requireNonNull(concertSeats)
			.stream()
			.map(obj -> (ConcertSeatResult)obj)
			.toList();
	}

	@Override
	public Long saveAllConcertScheduleSeats(final Long concertScheduleId,
		final List<ConcertSeatResult> concertScheduleSeatsResult) {
		final String concertSeatsKey = String.format(concertSeatsKeyFormat, concertScheduleId);

		final List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
			final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
			final RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

			final byte[] key = Objects.requireNonNull(keySerializer.serialize(concertSeatsKey));

			for (ConcertSeatResult concertSeatResult : concertScheduleSeatsResult) {
				connection.rPush(key, valueSerializer.serialize(concertSeatResult));
			}

			connection.expire(key, concertScheduleSeatTTL);

			return null;
		});

		return results.isEmpty() ? 0 : Long.valueOf(results.size() - 1);
	}

	@Override
	public void updateToAvailableSeats(final List<ConcertSeatOccupyCancelRequest> concertSeatOccupyCancelRequests) {
		for (ConcertSeatOccupyCancelRequest request : concertSeatOccupyCancelRequests) {
			final String concertSeatsKey = String.format(concertSeatsKeyFormat, request.scheduleId());

			final ConcertSeatResult concertSeatResult = new ConcertSeatResult(request.seatId(), request.seatNumber());
			redisTemplate.opsForHash().put(concertSeatsKey, request.seatId(), concertSeatResult);
		}
	}

	@Override
	public void updateToUnavailableSeat(final Long scheduleId, final Long seatId, final Long seatNumber) {
		final String concertSeatsKey = String.format(concertSeatsKeyFormat, scheduleId);
		redisTemplate.opsForList().remove(concertSeatsKey, 1, new ConcertSeatResult(seatId, seatNumber));
	}
}
