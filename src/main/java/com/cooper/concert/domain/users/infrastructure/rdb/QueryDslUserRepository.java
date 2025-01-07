package com.cooper.concert.domain.users.infrastructure.rdb;

import static com.cooper.concert.domain.users.models.QUser.user;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.models.User;
import com.cooper.concert.domain.users.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class QueryDslUserRepository implements UserRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public User findByAltId(final UUID altId) {
		return queryFactory.selectFrom(user)
			.where(user.altId.eq(altId))
			.fetchFirst();
	}
}
