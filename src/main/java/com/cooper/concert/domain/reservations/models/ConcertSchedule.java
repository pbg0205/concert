package com.cooper.concert.domain.reservations.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleCreationException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long concertId;

	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime startAt;

	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime endAt;

	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private ConcertSchedule(final Long concertId, final LocalDateTime startAt, final LocalDateTime endAt) {
		this.concertId = concertId;
		this.startAt = startAt;
		this.endAt = endAt;
	}

	public static ConcertSchedule create(final Long concertId, final LocalDateTime startAt, final LocalDateTime endAt) {
		if (startAt.isAfter(endAt) || startAt.isEqual(endAt)) {
			throw new ConcertScheduleCreationException(ConcertErrorType.INVALID_CONCERT_SCHEDULE);
		}

		return new ConcertSchedule(concertId, startAt, endAt);
	}
}
