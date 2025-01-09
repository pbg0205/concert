package com.cooper.concert.domain.payments.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;

@Getter
public abstract class PaymentException extends RuntimeException {
	private final PaymentErrorType errorType;

	public PaymentException(final PaymentErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
