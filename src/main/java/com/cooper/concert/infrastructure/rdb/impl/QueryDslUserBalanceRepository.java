package com.cooper.concert.infrastructure.rdb.impl;

import static com.cooper.concert.domain.QUserBalance.userBalance;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.business.repository.UserBalanceRepository;
import com.cooper.concert.domain.UserBalance;

@Repository
@RequiredArgsConstructor
public class QueryDslUserBalanceRepository implements UserBalanceRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public UserBalance findByUserId(final Long userId) {
		return queryFactory.selectFrom(userBalance)
			.where(userBalance.userId.eq(userId))
			.fetchFirst();
	}
}
