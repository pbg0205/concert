package com.cooper.concert.domain.reservations.models;

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

import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationCanceledException;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationReservedException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "altId")})
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long seatId;

	@Column(nullable = false)
	private UUID altId;

	@Column(nullable = false, length = 20)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Getter(AccessLevel.PRIVATE)
	private ReservationStatus status;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private Reservation(final Long userId, final Long seatId, final UUID altId, final ReservationStatus status) {
		this.userId = userId;
		this.seatId = seatId;
		this.altId = altId;
		this.status = status;
	}

	public static Reservation createPendingReservation(final Long userId, final Long seatId, final UUID altId) {
		return new Reservation(userId, seatId, altId, ReservationStatus.PENDING);
	}

	public boolean cancel() {
		if (this.status == ReservationStatus.RESERVED) {
			throw new ReservationReservedException(ReservationErrorType.RESERVATION_RESERVED);
		}

		this.status = ReservationStatus.CANCELED;
		return true;
	}

	public boolean complete() {
		if (this.status == ReservationStatus.CANCELED) {
			throw new ReservationCanceledException(ReservationErrorType.RESERVATION_CANCELED);
		}

		if (this.status  == ReservationStatus.RESERVED) {
			throw new ReservationReservedException(ReservationErrorType.RESERVATION_RESERVED);
		}

		this.status = ReservationStatus.RESERVED;
		return true;
	}
}
