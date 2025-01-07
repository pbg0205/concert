package com.cooper.concert.domain.queues.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userId")})
public class QueueToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	}

	public static QueueToken createWaitingToken(final UUID tokenId, final Long userId) {
		return new QueueToken(tokenId, userId, QueueTokenStatus.WAITING); // TODO UUIDv7 코드 작성하기
	}
}
