package com.cooper.concert.domain.payments.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

	@Test
	@DisplayName("결제 객체 생성하면 상태는 PENDING")
	void 결제_객체_생성하면_상태는_PENDING() {
		// given
		final UUID altId = UUID.fromString("01944a4c-5980-7920-97e3-b981fec463d5");

		// when
		final Payment sut = Payment.createPendingPayment(altId);

		// then
		assertThat(sut).extracting("status").isEqualTo(PaymentStatus.PENDING);
	}
}
