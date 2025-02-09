package com.cooper.concert.interfaces.api.reservations.usecase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.ConcertScheduleCacheService;
import com.cooper.concert.domain.reservations.service.ConcertScheduleReadService;
import com.cooper.concert.domain.reservations.service.ConcertScheduleSeatsCacheService;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleSeatsResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.interfaces.components.annotations.Facade;

@Facade
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertScheduleReadUseCase {

	private final ConcertScheduleReadService concertScheduleReadService;
	private final ConcertScheduleCacheService concertScheduleCacheService;
	private final ConcertScheduleSeatsCacheService concertScheduleSeatsCacheService;
	private final RedissonClient redissonClient;

	public List<ConcertScheduleResult> readAllScheduleByConcertIdAndPaging(
		final Long concertId, final Integer offset, final Integer limit) {
		return concertScheduleReadService.findByAllByConcertIdAndPaging(concertId, offset, limit);
	}

	public ConcertScheduleSeatsResult readAvailableSeatsByScheduleId(final Long concertId,
		final Long concertScheduleId) {
		if (!concertScheduleSeatsCacheService.existsConcertSeatsByConcertScheduleId(concertScheduleId)) {
			putScheduleAndSeatsCacheWithDistributedLock(concertId, concertScheduleId);
		}

		final ConcertScheduleResult concertScheduleResult =
			concertScheduleCacheService.findById(concertId, concertScheduleId);

		final List<ConcertSeatResult> availableSeatsResult =
			concertScheduleSeatsCacheService.findAvailableSeatsByScheduleId(concertScheduleId);

		return new ConcertScheduleSeatsResult(concertScheduleResult.startDateTime(), availableSeatsResult);
	}

	private void putScheduleAndSeatsCacheWithDistributedLock(final Long concertId, final Long concertScheduleId) {
		RLock rLock = redissonClient.getLock("CONCERT:SEATS:" + concertScheduleId);
		try {
			rLock.tryLock(5L, 3L, TimeUnit.SECONDS);

			putConcertSchedule(concertId, concertScheduleId);
			putConcertSchedulesSeats(concertScheduleId);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			rLock.unlock();
		}
	}

	private void putConcertSchedule(final Long concertId, final Long concertScheduleId) {
		final ConcertScheduleResult concertScheduleResult =
			concertScheduleReadService.findConcertScheduleById(concertScheduleId);

		concertScheduleCacheService.saveConcertScheduleById(concertId, concertScheduleResult);
	}

	private void putConcertSchedulesSeats(final Long concertScheduleId) {
		final ConcertScheduleSeatsResult concertScheduleSeatsResult =
			concertScheduleReadService.findAvailableSeatsByScheduleId(concertScheduleId);

		concertScheduleSeatsCacheService.saveAllConcertScheduleSeats(concertScheduleId,
			concertScheduleSeatsResult.availableSeats());
	}
}
