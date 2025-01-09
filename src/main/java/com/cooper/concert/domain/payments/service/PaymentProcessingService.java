package com.cooper.concert.domain.payments.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCompleteInfo;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCreationInfo;
import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentNotFoundException;
import com.cooper.concert.domain.payments.service.repository.PaymentCommandRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentProcessingService {

	private final PaymentAltIdGenerator paymentAltIdGenerator;
	private final PaymentCommandRepository paymentCommandRepository;

	public PaymentCreationInfo createPendingPayment(final Long reservationId) {
		final UUID paymentAltId = paymentAltIdGenerator.generatePaymentAltId();
		final Payment savedPayment = paymentCommandRepository.save(Payment.createPendingPayment(paymentAltId, reservationId));
		return new PaymentCreationInfo(savedPayment.getAltId());
	}

	public PaymentCompleteInfo completePayment(final UUID paymentAltId) {
		final Payment payment = Optional.ofNullable(paymentCommandRepository.findByAltId(paymentAltId))
			.orElseThrow(() -> new PaymentNotFoundException(PaymentErrorType.PAYMENT_KEY_NOT_FOUND));
		payment.complete();
		return new PaymentCompleteInfo(payment.getReservationId());
	}
}
