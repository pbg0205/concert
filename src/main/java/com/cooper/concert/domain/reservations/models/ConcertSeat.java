package com.cooper.concert.domain.reservations.models;

import java.time.LocalDateTime;

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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long scheduleId;

	@Column(nullable = false)
	private Long seatNumber;

	@Column(nullable = false, length = 20)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private ConcertSeatStatus status;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

}
