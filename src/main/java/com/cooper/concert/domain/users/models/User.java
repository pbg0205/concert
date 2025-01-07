package com.cooper.concert.domain.users.models;

import static com.cooper.concert.domain.users.service.errors.UserErrorType.USER_NAME_EMPTY;
import static com.cooper.concert.domain.users.service.errors.UserErrorType.USER_NAME_MAX_LENGTH_EXCEEDS;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cooper.concert.domain.users.service.errors.exception.InvalidUserNameException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"alt_id"})})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
	private UUID altId;

	@CreationTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime modifiedAt;

	private User(final String name, final UUID altId) {
		validateName(name);
		this.name = name;
		this.altId = altId;
	}

	private void validateName(final String name) {
		if (name.isEmpty()) {
			throw new InvalidUserNameException(USER_NAME_EMPTY);
		}
		if (name.length() > 100) {
			throw new InvalidUserNameException(USER_NAME_MAX_LENGTH_EXCEEDS);
		}
	}

	public static User create(final String name, final UUID uuid) {
		return new User(name, uuid);
	}
}
