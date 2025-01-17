package com.cooper.concert.domain.users.infrastructure.rdb;

import static com.cooper.concert.domain.users.models.QUserBalance.userBalance;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.service.repository.UserBalanceQueryRepository;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBalanceQueryRepositoryDsl implements UserBalanceQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public UserBalanceReadResult findByUserId(final Long userId) {
		return queryFactory.select(Projections.constructor(UserBalanceReadResult.class, userBalance.point))
			.from(userBalance)
			.where(userBalance.userId.eq(userId))
			.fetchFirst();
	}

}
