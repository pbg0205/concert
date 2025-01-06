package com.cooper.concert.infrastructure.rdb.impl;

import static com.cooper.concert.domain.QUser.user;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.business.repository.UserRepository;
import com.cooper.concert.domain.User;

@Repository
@RequiredArgsConstructor
public class QuerydslUserRepository implements UserRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public User findByAltId(final UUID altId) {
		return queryFactory.selectFrom(user)
			.where(user.altId.eq(altId))
			.fetchFirst();
	}
}
