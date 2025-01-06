package com.cooper.concert.business.errors.exception;

import lombok.Getter;

import com.cooper.concert.business.errors.UserErrorType;

@Getter
public abstract class UserException extends RuntimeException {
	private final UserErrorType errorType;

	public UserException(final UserErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
