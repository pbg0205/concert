package com.cooper.concert.business.errors.exception;

import lombok.Getter;

import com.cooper.concert.business.errors.UserErrorType;

@Getter
public class UserBalanceNotFoundException extends UserException {
	public UserBalanceNotFoundException(final UserErrorType errorType) {
		super(errorType);
	}
}
