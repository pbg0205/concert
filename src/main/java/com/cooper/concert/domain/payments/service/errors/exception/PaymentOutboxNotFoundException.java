package com.cooper.concert.domain.payments.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;

@Getter
public class PaymentOutboxNotFoundException extends PaymentException {
	public PaymentOutboxNotFoundException(final PaymentErrorType errorType) {
		super(errorType);
	}

}
