package com.cooper.concert.domain.payments.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCreationInfo;
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
}
