package com.cooper.concert.domain.payments.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.RdbRepositoryTest;
import com.cooper.concert.domain.payments.models.Payment;

@RdbRepositoryTest(basePackages = {
	"com.cooper.concert.domain.payments.infrastructure.rdb",
	"com.cooper.concert.storage.rdb.jpa"})
class PaymentCommandRepositoryTest {

	@Autowired
	private PaymentCommandRepository paymentCommandRepository;

	@Test
	@DisplayName("결제 객체 생성 성공")
	void 결제_객체_생성_성공() {
		// given
		final UUID paymentAltId = UUID.fromString("01944a6e-28a5-75d8-a25e-33738ed269c1");
		final Long reservationId = 1L;
		final Payment payment = Payment.createPendingPayment(paymentAltId, reservationId);

		// when
		final Payment sut = paymentCommandRepository.save(payment);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).isNotNull();
			softAssertions.assertThat(sut).extracting("id").isNotNull();
		});
	}

	@Test
	@DisplayName("결제 대체키를 기반한 결제 조회 성공")
	@Sql("classpath:sql/payment_repository.sql")
	void 결제_대체키를_기반한_결제_조회_성공() {
		// given
		final UUID paymentAltId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

		// when
		final Payment sut = paymentCommandRepository.findByAltId(paymentAltId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut).isNotNull();
			softAssertions.assertThat(sut).extracting("id").isNotNull();
		});
	}

	@Test
	@DisplayName("삭제 예정 결제 목록 조회")
	@Sql("classpath:sql/payment_for_cancel_repository.sql")
	void 삭제_예정_결제_목록_조회() {
		// given
		final List<Long> reservationIds = List.of(12345L, 12346L, 12347L, 12348L, 12349L, 12350L);
		final String paymentStatus = "PENDING";

		// when
		final List<Payment> sut = paymentCommandRepository.findAllByReservationIds(reservationIds, paymentStatus);

		// then
		assertThat(sut).hasSize(4);
	}

}
