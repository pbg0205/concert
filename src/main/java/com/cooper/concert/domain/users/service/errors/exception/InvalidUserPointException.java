package com.cooper.concert.domain.users.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.users.service.errors.UserErrorType;

@Getter
public class InvalidUserPointException extends UserException {
	public InvalidUserPointException(final UserErrorType errorType) {
		super(errorType);
	}
}
