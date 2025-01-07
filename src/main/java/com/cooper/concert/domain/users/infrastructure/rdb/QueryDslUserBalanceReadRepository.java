package com.cooper.concert.domain.users.infrastructure.rdb;

import static com.cooper.concert.domain.users.models.QUserBalance.userBalance;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.repository.UserBalanceRepository;

@Repository
@RequiredArgsConstructor
public class QueryDslUserBalanceReadRepository implements UserBalanceRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public UserBalance findByUserId(final Long userId) {
		return queryFactory.selectFrom(userBalance)
			.where(userBalance.userId.eq(userId))
			.fetchFirst();
	}
}
