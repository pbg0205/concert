package com.cooper.concert.domain.payments.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.models.PaymentStatus;
import com.cooper.concert.domain.payments.service.repository.PaymentCommandRepository;

@Service
@RequiredArgsConstructor
public class PaymentCancelService {

	private final PaymentCommandRepository paymentCommandRepository;
	
	public Integer cancelPayments(final List<Long> reservationIds) {
		final String paymentStatus = PaymentStatus.PENDING.name();
		final List<Payment> payments = paymentCommandRepository.findAllByReservationIds(reservationIds, paymentStatus);
		return payments.stream().map(Payment::cancel).toList().size();
	}
}
