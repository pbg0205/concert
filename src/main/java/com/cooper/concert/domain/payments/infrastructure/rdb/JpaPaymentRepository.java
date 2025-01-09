package com.cooper.concert.domain.payments.infrastructure.rdb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.payments.models.Payment;

public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
}
