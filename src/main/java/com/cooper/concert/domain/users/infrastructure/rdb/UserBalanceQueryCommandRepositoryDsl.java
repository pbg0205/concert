package com.cooper.concert.domain.users.infrastructure.rdb;

import static com.cooper.concert.domain.users.models.QUserBalance.userBalance;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.repository.UserBalanceCommandRepository;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserBalanceQueryCommandRepositoryDsl implements UserBalanceCommandRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public UserBalance findByUserIdForUpdate(final Long userId) {
		return queryFactory.selectFrom(userBalance)
			.where(userBalance.userId.eq(userId))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchFirst();
	}
}
