package com.cooper.concert.domain.reservations.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class ConcertSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long scheduleId;

	@Column(nullable = false)
	private Long seatNumber;

	@Column(nullable = false)
	@ColumnDefault("0")
	private Long price;

	@Column(nullable = false, length = 20)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Getter(AccessLevel.PRIVATE)
	private ConcertSeatStatus status;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private ConcertSeat(final Long scheduleId, final Long seatNumber, final ConcertSeatStatus status) {
		this.scheduleId = scheduleId;
		this.seatNumber = seatNumber;
		this.status = status;
	}

	public static ConcertSeat createAvailableSeat(final Long scheduleId, final Long seatNumber) {
		return new ConcertSeat(scheduleId, seatNumber, ConcertSeatStatus.AVAILABLE);
	}

	public boolean updateUnavailable() {
		if (this.status != ConcertSeatStatus.AVAILABLE) {
			return false;
		}

		this.status = ConcertSeatStatus.UNAVAILABLE;
		return true;
	}

	public boolean updateAvailable() {
		if (this.status != ConcertSeatStatus.UNAVAILABLE) {
			return false;
		}

		this.status = ConcertSeatStatus.AVAILABLE;
		return true;
	}

	public boolean isUnavailable() {
		return this.status == ConcertSeatStatus.UNAVAILABLE;
	}
}
