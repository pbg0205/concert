package com.cooper.concert.domain.queues.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QueueTokenExpireOutbox {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String topic;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String payload;

	@Column(nullable = false, columnDefinition = "BINARY(16)")
	private UUID paymentId;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private QueueTokenExpireOutbox(final String topic, final String type, final String payload, final UUID paymentId) {
		this.topic = topic;
		this.type = type;
		this.payload = payload;
		this.paymentId = paymentId;
		this.createdAt = LocalDateTime.now();
		this.modifiedAt = LocalDateTime.now();
	}

	public static QueueTokenExpireOutbox createInitStatus(final String topic, final String payload, final UUID paymentId) {
		return new QueueTokenExpireOutbox(topic, QueueTokenOutboxStatus.INIT.name(), payload, paymentId);
	}

	public boolean updateSuccess() {
		this.type = QueueTokenOutboxStatus.SEND_SUCCESS.name();
		return true;
	}

	public boolean updateFail() {
		this.type = QueueTokenOutboxStatus.SEND_FAIL.name();
		return true;
	}
}
