package com.cooper.concert.domain.payments.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCreationInfo;
import com.cooper.concert.domain.payments.service.repository.PaymentCommandRepository;

class PaymentProcessingServiceTest {

	private PaymentProcessingService paymentProcessingService;

	private PaymentAltIdGenerator paymentAltIdGenerator;

	private PaymentCommandRepository paymentCommandRepository;

	@BeforeEach
	void setUp() {
		this.paymentAltIdGenerator = new PaymentAltIdGenerator();
		this.paymentCommandRepository = Mockito.mock(PaymentCommandRepository.class);
		this.paymentProcessingService = new PaymentProcessingService(paymentAltIdGenerator, paymentCommandRepository);
	}

	@Test
	@DisplayName("결제 생성 성공")
	void 결제_생성_성공() {
		// given
		final UUID paymentAltId = paymentAltIdGenerator.generatePaymentAltId();
		final Payment payment = Payment.createPendingPayment(paymentAltId);

		when(paymentCommandRepository.save(any())).thenReturn(payment);

		// when
		final PaymentCreationInfo sut = paymentProcessingService.createPendingPayment(1L);

		// then
		assertThat(sut.paymentAltId()).isEqualTo(paymentAltId);
	}
}
