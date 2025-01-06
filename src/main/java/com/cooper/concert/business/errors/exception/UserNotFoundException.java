package com.cooper.concert.business.errors.exception;

import lombok.Getter;

import com.cooper.concert.business.errors.UserErrorType;

@Getter
public class UserNotFoundException extends UserException {
	public UserNotFoundException(final UserErrorType errorType) {
		super(errorType);
	}
}
