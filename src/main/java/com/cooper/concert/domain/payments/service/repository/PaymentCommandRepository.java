package com.cooper.concert.domain.payments.service.repository;

import java.util.UUID;

import com.cooper.concert.domain.payments.models.Payment;

public interface PaymentCommandRepository {
	Payment save(Payment payment);
	Payment findByAltId(UUID paymentAltId);
}
