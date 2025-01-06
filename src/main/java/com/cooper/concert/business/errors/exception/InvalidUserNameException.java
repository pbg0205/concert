package com.cooper.concert.business.errors.exception;

import lombok.Getter;

import com.cooper.concert.business.errors.UserErrorType;

@Getter
public class InvalidUserNameException extends UserException {
	public InvalidUserNameException(final UserErrorType errorType) {
		super(errorType);
	}
}
