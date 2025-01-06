package com.cooper.concert.business.errors.exception;

import lombok.Getter;

import com.cooper.concert.business.errors.UserErrorType;

@Getter
public class InvalidUserPointException extends UserException {
	public InvalidUserPointException(final UserErrorType errorType) {
		super(errorType);
	}
}
