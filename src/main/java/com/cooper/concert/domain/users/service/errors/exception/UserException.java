package com.cooper.concert.domain.users.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.users.service.errors.UserErrorType;

@Getter
public abstract class UserException extends RuntimeException {
	private final UserErrorType errorType;

	public UserException(final UserErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
