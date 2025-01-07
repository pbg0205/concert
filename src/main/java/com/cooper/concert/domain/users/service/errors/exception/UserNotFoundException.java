package com.cooper.concert.domain.users.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.users.service.errors.UserErrorType;

@Getter
public class UserNotFoundException extends UserException {
	public UserNotFoundException(final UserErrorType errorType) {
		super(errorType);
	}
}
