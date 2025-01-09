package com.cooper.concert.domain.payments.infrastructure.rdb;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.repository.PaymentCommandRepository;

@Repository
@RequiredArgsConstructor
public class PaymentCommandRepositoryDsl implements PaymentCommandRepository {

	private final JpaPaymentRepository jpaPaymentRepository;

	@Override
	public Payment save(final Payment payment) {
		return jpaPaymentRepository.save(payment);
	}
}
