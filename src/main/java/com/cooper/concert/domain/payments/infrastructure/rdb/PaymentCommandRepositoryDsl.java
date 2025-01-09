package com.cooper.concert.domain.payments.infrastructure.rdb;

import static com.cooper.concert.domain.payments.models.QPayment.payment;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.models.Payment;
import com.cooper.concert.domain.payments.service.repository.PaymentCommandRepository;

@Repository
@RequiredArgsConstructor
public class PaymentCommandRepositoryDsl implements PaymentCommandRepository {

	private final JpaPaymentRepository jpaPaymentRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public Payment save(final Payment payment) {
		return jpaPaymentRepository.save(payment);
	}

	@Override
	public Payment findByAltId(final UUID paymentAltId) {
		return queryFactory.selectFrom(payment)
			.where(payment.altId.eq(paymentAltId))
			.fetchOne();
	}

	@Override
	public List<Payment> findAllByReservationIds(final List<Long> reservationIds, String paymentStatus) {
		return queryFactory.selectFrom(payment)
			.where(payment.reservationId.in(reservationIds).and(payment.status.stringValue().eq(paymentStatus)))
			.fetch();
	}
}
