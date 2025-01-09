package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QConcertSeat.concertSeat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.ConcertSeat;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatCommandRepository;

@Repository
@RequiredArgsConstructor
public class ConcertSeatCommandRepositoryDsl implements ConcertSeatCommandRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public ConcertSeat findById(final Long id) {
		return queryFactory.selectFrom(concertSeat)
			.where(concertSeat.id.eq(id))
			.fetchOne();
	}

	@Override
	public List<ConcertSeat> findAllByIdsAndSeatStatus(final List<Long> concertSeatIds, final String seatStatus) {
		return queryFactory.selectFrom(concertSeat)
			.where(concertSeat.id.in(concertSeatIds).and(concertSeat.status.stringValue().eq(seatStatus)))
			.fetch();
	}
}
