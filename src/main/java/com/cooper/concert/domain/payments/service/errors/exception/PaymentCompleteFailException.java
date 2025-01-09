package com.cooper.concert.domain.payments.service.errors.exception;

import lombok.Getter;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;

@Getter
public class PaymentCompleteFailException extends PaymentException {
	public PaymentCompleteFailException(final PaymentErrorType errorType) {
		super(errorType);
	}
}
