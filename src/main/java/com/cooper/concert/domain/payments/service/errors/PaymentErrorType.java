package com.cooper.concert.domain.payments.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorType {

	PAYMENT_KEY_NOT_FOUND(PaymentErrorCode.ERROR_PAYMENT01, "결제 키를 찾을 수 없습니다"),
	PAYMENT_CANCELED(PaymentErrorCode.ERROR_PAYMENT02, "결제가 취소된 상태 입니다."),
	PAYMENT_COMPLETED(PaymentErrorCode.ERROR_PAYMENT03, "결제가 이미 완료된 상태입니다.");

	private final PaymentErrorCode errorCode;
	private final String message;
}
