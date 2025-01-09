package com.cooper.concert.domain.payments.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCompleteInfo;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCreationInfo;
import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentNotFoundException;
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
		final Long reservationId = 1L;
		final Payment payment = Payment.createPendingPayment(paymentAltId, reservationId);

		when(paymentCommandRepository.save(any())).thenReturn(payment);

		// when
		final PaymentCreationInfo sut = paymentProcessingService.createPendingPayment(1L);

		// then
		assertThat(sut.paymentAltId()).isEqualTo(paymentAltId);
	}

	@Test
	@DisplayName("결제 대체키 기반 결제 조회 실패하면 완료 실패")
	void 결제_대체키_기반_결제_조회_실패하면_완료_실패() {
		// given
		final UUID paymentAltId = paymentAltIdGenerator.generatePaymentAltId();

		when(paymentCommandRepository.findByAltId(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> paymentProcessingService.completePayment(paymentAltId))
			.isInstanceOf(PaymentNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(errorType).isEqualTo(PaymentErrorType.PAYMENT_KEY_NOT_FOUND));
	}

	@Test
	@DisplayName("결제 완료 성공하면 true 반환")
	void 결제_완료_성공() {
		// given
		final UUID paymentAltId = paymentAltIdGenerator.generatePaymentAltId();
		final Long reservationId = 1L;
		final Payment payment = Payment.createPendingPayment(paymentAltId, reservationId);

		when(paymentCommandRepository.findByAltId(any())).thenReturn(payment);

		// when
		final PaymentCompleteInfo sut = paymentProcessingService.completePayment(paymentAltId);

		// then
		assertThat(sut.reservationId()).isNotNull();
	}
}
