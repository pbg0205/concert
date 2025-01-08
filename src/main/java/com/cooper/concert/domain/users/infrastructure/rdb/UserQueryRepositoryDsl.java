package com.cooper.concert.domain.users.infrastructure.rdb;

import static com.cooper.concert.domain.users.models.QUser.user;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.users.service.repository.UserQueryRepository;
import com.cooper.concert.domain.users.service.response.UserReadResult;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryRepositoryDsl implements UserQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public UserReadResult findByAltId(final UUID altId) {
		return queryFactory.select(Projections.constructor(UserReadResult.class, user.id, user.name))
			.from(user)
			.where(user.altId.eq(altId))
			.fetchFirst();
	}
}
