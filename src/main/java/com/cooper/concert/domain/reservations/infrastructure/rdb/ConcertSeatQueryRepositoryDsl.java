package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QConcertSeat.concertSeat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatResult;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatQueryRepository;

@Repository
@RequiredArgsConstructor
public class ConcertSeatQueryRepositoryDsl implements ConcertSeatQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ConcertSeatResult> findConcertSeatsByScheduleIdAndStatus(
		final Long scheduleId, final String status) {
		return queryFactory.select(
				Projections.constructor(ConcertSeatResult.class, concertSeat.id, concertSeat.seatNumber))
			.from(concertSeat)
			.where(concertSeat.scheduleId.eq(scheduleId).and(concertSeat.status.stringValue().eq(status)))
			.fetch();
	}

	@Override
	public ConcertSeatPriceInfo findConcertSeatPriceInfoById(final Long seatId) {
		return queryFactory.select(Projections.constructor(ConcertSeatPriceInfo.class,
				concertSeat.id, concertSeat.seatNumber, concertSeat.price))
			.from(concertSeat)
			.where(concertSeat.id.eq(seatId))
			.fetchOne();
	}
}
