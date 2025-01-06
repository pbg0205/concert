package com.cooper.concert.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

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

import com.cooper.concert.business.errors.UserErrorType;
import com.cooper.concert.business.errors.exception.InvalidUserPointException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id"})})
@DynamicUpdate
public class UserBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column
	private Long point;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private UserBalance(final Long userId, final Long point) {
		validatePoint(point, UserErrorType.USER_BALANCE_NEGATIVE);
		this.userId = userId;
		this.point = point;
	}

	private void validatePoint(final Long point, UserErrorType errorType) {
		if (point < 0) {
			throw new InvalidUserPointException(errorType);
		}
	}

	public static UserBalance create(final Long userId, final Long point) {
		return new UserBalance(userId, point);
	}

	public Long addPoint(final Long point) {
		validatePoint(point, UserErrorType.CHARGING_POINT_NEGATIVE);
		this.point += point;
		return this.point;
	}
}
