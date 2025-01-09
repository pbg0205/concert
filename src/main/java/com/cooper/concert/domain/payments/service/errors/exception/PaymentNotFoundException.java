package com.cooper.concert.domain.payments.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;

@Getter
public class PaymentNotFoundException extends PaymentException {
	public PaymentNotFoundException(final PaymentErrorType errorType) {
		super(errorType);
	}
}
