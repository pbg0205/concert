package com.cooper.concert.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.users.models.UserBalance;

public interface UserBalanceTestRepository extends JpaRepository<UserBalance, Long> {
	UserBalance findUserBalanceByUserId(Long userId);
}
