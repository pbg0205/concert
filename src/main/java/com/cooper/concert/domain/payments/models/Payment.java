package com.cooper.concert.domain.payments.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentCompleteFailException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "altId")})
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Column(nullable = false)
	private UUID altId;

	@Getter
	@Column(nullable = false)
	private Long reservationId;

	@Column(nullable = false, length = 20)
	@Getter(AccessLevel.PRIVATE)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private PaymentStatus status;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private Payment(final UUID altId, final Long reservationId, final PaymentStatus status) {
		this.altId = altId;
		this.reservationId = reservationId;
		this.status = status;
	}

	public static Payment createPendingPayment(final UUID paymentAltId, final Long reservationId) {
		return new Payment(paymentAltId, reservationId, PaymentStatus.PENDING);
	}

	public boolean complete() {
		if (this.status == PaymentStatus.CANCELLED) {
			throw new PaymentCompleteFailException(PaymentErrorType.PAYMENT_CANCELED);
		}

		if (this.status == PaymentStatus.COMPLETED) {
			throw new PaymentCompleteFailException(PaymentErrorType.PAYMENT_COMPLETED);
		}

		this.status = PaymentStatus.COMPLETED;
		return true;
	}
}
