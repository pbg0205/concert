package com.cooper.concert.domain.users.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.users.service.errors.UserErrorType;

@Getter
public class UserBalanceNotFoundException extends UserException {
	public UserBalanceNotFoundException(final UserErrorType errorType) {
		super(errorType);
	}
}
