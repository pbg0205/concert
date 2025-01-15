package com.cooper.concert.api.components.dto;

import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.InvalidTokenException;

@Getter
@RequiredArgsConstructor
public class TokenHeaderData {

	private final UUID tokenId;

	public TokenHeaderData(final String tokenId) {
		this.tokenId = convertTokenToUUID(tokenId);
	}

	private UUID convertTokenToUUID(final String tokenId) {
		try {
			return UUID.fromString(Objects.requireNonNull(tokenId));
		} catch (IllegalArgumentException exception) {
			throw new InvalidTokenException(TokenErrorType.TOKEN_FORMAT_INVALID);
		} catch (NullPointerException exception) {
			throw new InvalidTokenException(TokenErrorType.TOKEN_EMPTY);
		}
	}

}
