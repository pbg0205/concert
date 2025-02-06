package com.cooper.concert.domain.queues.infrastructure.jwt;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.cooper.concert.domain.queues.service.jwt.QueueTokenGenerator;

@Component
public class JwtQueueTokenGenerator implements QueueTokenGenerator {

	@Value("${token.secret-key}")
	private String secretKey;

	@Value("${token.valid-seconds}")
	private Long validSeconds;

	@Override
	public String generateJwt(final Long userId, final Instant issuedAt) {
		Instant expiredAt = issuedAt.plusSeconds(validSeconds);
		return Jwts.builder()
			.claim("userId", userId)
			.issuedAt(Date.from(issuedAt))
			.expiration(Date.from(expiredAt))
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}

}
