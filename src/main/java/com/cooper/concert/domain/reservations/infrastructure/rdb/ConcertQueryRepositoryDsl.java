package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QConcert.concert;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.repository.ConcertQueryRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertQueryRepositoryDsl implements ConcertQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsById(final Long id) {
		return queryFactory.select(concert.id)
			.from(concert)
			.where(concert.id.eq(id))
			.fetchOne() != null;
	}
}
