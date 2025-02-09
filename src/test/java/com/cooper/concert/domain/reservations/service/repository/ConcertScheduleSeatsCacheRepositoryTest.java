package com.cooper.concert.domain.reservations.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.cooper.concert.domain.reservations.service.dto.request.ConcertSeatOccupyCancelRequest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertSeatNotFoundException;

@SpringBootTest
class ConcertScheduleSeatsCacheRepositoryTest {

	@Value("${concert.schedules.available-seats.cache.format}")
	private String concertSeatsKeyFormat;

	@Autowired
	private ConcertScheduleSeatsCacheRepository concertScheduleSeatsCacheRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void tearDown() {
		final Set<String> allKeys = redisTemplate.keys("*");
		redisTemplate.delete(allKeys);
	}

	@Test
	@DisplayName("콘서트 좌석 존재하면 존재 여부 조회 성공")
	void 콘서트_좌석_있을_경우_존재_여부_조회_성공() {
		// given
		final Long concertScheduleId = 1L;
		final String concertScheduleKey = String.format(concertSeatsKeyFormat, concertScheduleId);

		redisTemplate.opsForList().rightPushAll(concertScheduleKey,
			new ConcertSeatResult(1L, 1L),
			new ConcertSeatResult(2L, 2L),
			new ConcertSeatResult(2L, 3L));

		// when
		final boolean sut = concertScheduleSeatsCacheRepository.existsByConcertScheduleId(concertScheduleId);

		// then
		assertThat(sut).isTrue();
	}

	@Test
	@DisplayName("콘서트 좌석 없을 경우 존여 여부 조회 성공")
	void 콘서트_좌석_없을_경우_존여_여부_조회_성공() {
		// given
		final Long concertScheduleId = 1L;

		// when
		final boolean sut = concertScheduleSeatsCacheRepository.existsByConcertScheduleId(concertScheduleId);

		// then
		assertThat(sut).isFalse();
	}

	@Test
	@DisplayName("콘서트 좌석 목록 조회 성공")
	void 콘서트_좌석_목록_조회_성공() {
		// given
		final Long concertScheduleId = 1L;
		final String concertScheduleKey = String.format(concertSeatsKeyFormat, concertScheduleId);

		redisTemplate.opsForList().rightPushAll(concertScheduleKey,
			new ConcertSeatResult(1L, 1L),
			new ConcertSeatResult(2L, 2L),
			new ConcertSeatResult(3L, 3L));

		// when
		final List<ConcertSeatResult> availableSeats =
			concertScheduleSeatsCacheRepository.findAvailableSeatsByScheduleId(concertScheduleId);

		// then
		assertThat(availableSeats).hasSize(3);
	}

	@Test
	@DisplayName("콘서트 좌석 목록이 없을 경우 조회 실패")
	void 콘서트_좌석_목록이_없을_경우_조회_실패() {
		// given
		final Long concertScheduleId = 1L;

		// when. then
		assertThatThrownBy(() -> concertScheduleSeatsCacheRepository.findAvailableSeatsByScheduleId(concertScheduleId))
			.isInstanceOf(ConcertSeatNotFoundException.class)
			.extracting("errorType").isEqualTo(ConcertErrorType.CONCERT_SEAT_NOT_FOUND);
	}

	@Test
	@DisplayName("콘서트 좌석 목록 캐싱 성공")
	void 콘서트_좌석_목록_캐싱_성공() {
		// given
		final Long concertScheduleId = 1L;
		List<ConcertSeatResult> concertScheduleSeatsResult = List.of(
			new ConcertSeatResult(1L, 1L),
			new ConcertSeatResult(1L, 2L),
			new ConcertSeatResult(1L, 3L),
			new ConcertSeatResult(1L, 4L),
			new ConcertSeatResult(1L, 5L));

		// when
		final Long sut = concertScheduleSeatsCacheRepository.saveAllConcertScheduleSeats(
			concertScheduleId, concertScheduleSeatsResult);

		// then
		assertThat(sut).isEqualTo(5);
	}

	@Test
	@DisplayName("콘서트 좌석 점유 캐싱 해제 성공")
	void 콘서트_좌석_점유_캐싱_해제_성공() {
		// given
		final String concertScheduleKey = String.format(concertSeatsKeyFormat, 1L);

		List<ConcertSeatOccupyCancelRequest> availableSeatIds =
			List.of(
				new ConcertSeatOccupyCancelRequest(1L, 1L, 1L),
				new ConcertSeatOccupyCancelRequest(2L, 2L, 1L),
				new ConcertSeatOccupyCancelRequest(3L, 3L, 1L));

		// when
		concertScheduleSeatsCacheRepository.updateToAvailableSeats(availableSeatIds);

		// then
		final Set<Object> keySet = redisTemplate.opsForHash().keys(concertScheduleKey);
		assertThat(keySet).hasSize(3);
	}

}
