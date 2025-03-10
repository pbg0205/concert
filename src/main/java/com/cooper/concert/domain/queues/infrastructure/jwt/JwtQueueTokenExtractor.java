package com.cooper.concert.domain.queues.infrastructure.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.ExpiredQueueTokenException;
import com.cooper.concert.domain.queues.service.errors.exception.InvalidQueueTokenException;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;

@Component
public class JwtQueueTokenExtractor implements QueueTokenExtractor {

	@Value("${token.secret-key}")
	private String secretKey;

	@Override
	public Long extractUserIdFromToken(final String token) {
		try {
			final Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.build()
				.parseSignedClaims(token);

			return Long.parseLong(claimsJws.getPayload().get("userId").toString());
		} catch (ExpiredJwtException jwtException) {
			throw new ExpiredQueueTokenException(TokenErrorType.TOKEN_EXPIRED);
		} catch (MalformedJwtException malformedJwtException) {
			throw new InvalidQueueTokenException(TokenErrorType.TOKEN_FORMAT_INVALID);
		}
	}
}
