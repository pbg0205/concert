package com.cooper.concert.domain.users.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.users.service.errors.UserErrorType;

@Getter
public class InvalidUserNameException extends UserException {
	public InvalidUserNameException(final UserErrorType errorType) {
		super(errorType);
	}
}
