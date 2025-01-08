package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QConcertSchedule.concertSchedule;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleQueryRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertScheduleQueryRepositoryDsl implements ConcertScheduleQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ConcertScheduleResult> findByAllByConcertIdAndPaging(
		final Long concertId, final int offset, final int limit) {

		return queryFactory.select(Projections.constructor(ConcertScheduleResult.class,
			concertSchedule.id,
			concertSchedule.startAt))
			.from(concertSchedule)
			.where(concertSchedule.concertId.eq(concertId))
			.offset(offset)
			.limit(limit)
			.fetch();
	}
}
