package com.cooper.concert.domain.reservations.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertCreationException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long concertId;

	@Column(nullable = false, length = 300)
	private String name;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private Concert(final String name) {
		this.name = name;
	}

	public static Concert create(final String name) {
		if (name.isEmpty()) {
			throw new ConcertCreationException(ConcertErrorType.CONCERT_NAME_EMPTY);
		}
		return new Concert(name);
	}
}
