package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QConcertSeat.concertSeat;

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
}
