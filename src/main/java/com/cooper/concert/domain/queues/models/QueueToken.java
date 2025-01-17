package com.cooper.concert.domain.queues.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
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
@DynamicUpdate
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueueToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "BINARY(16)")
	private UUID tokenId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, length = 20)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private QueueTokenStatus status;

	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime expiredAt;

	private QueueToken(final UUID tokenId, final Long userId, final QueueTokenStatus status) {
		this.tokenId = tokenId;
		this.userId = userId;
		this.status = status;
		this.expiredAt = LocalDateTime.of(1970, 1, 1, 0, 0);
	}

	public static QueueToken createWaitingToken(final UUID tokenId, final Long userId) {
		return new QueueToken(tokenId, userId, QueueTokenStatus.WAITING);
	}

	public boolean updateCanceled() {
		if (this.status != QueueTokenStatus.WAITING) {
			return false;
		}

		this.status = QueueTokenStatus.CANCELLED;
		return true;
	}

	public boolean updateProcessing(final LocalDateTime currentTime, final Integer validTokenProcessingMinutes) {
		if (this.status != QueueTokenStatus.WAITING) {
			return false;
		}

		this.status = QueueTokenStatus.PROCESSING;
		this.expiredAt = currentTime.plusMinutes(validTokenProcessingMinutes);
		return true;
	}

	public boolean expire(final LocalDateTime expiredAt) {
		if (this.status != QueueTokenStatus.PROCESSING) {
			return false;
		}

		if (isValidExpiredTime(expiredAt)) {
			return false;
		}

		this.status = QueueTokenStatus.EXPIRED;
		return true;
	}

	public boolean complete(final LocalDateTime expiredAt) {
		this.status = QueueTokenStatus.COMPLETED;
		this.expiredAt = expiredAt;
		return true;
	}


	private boolean isValidExpiredTime(final LocalDateTime expiredAt) {
		return this.expiredAt.isAfter(expiredAt) || this.expiredAt.isEqual(expiredAt);
	}

	public boolean isValidProcessingToken(final LocalDateTime expiredAt) {
		if (this.status != QueueTokenStatus.PROCESSING) {
			return false;
		}

		return (this.expiredAt.isAfter(expiredAt) || this.expiredAt.isEqual(expiredAt));
	}
}
