package com.cooper.concert.domain.queues.service.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import com.cooper.concert.domain.queues.infrastructure.jwt.JwtQueueTokenExtractor;

class QueueTokenExtractorTest {

	private QueueTokenExtractor queueTokenExtractor;

	@BeforeEach
	public void setUp() {
		queueTokenExtractor = new JwtQueueTokenExtractor();
	}

	@Test
	@DisplayName("토큰에서 유저 식별자 추출 성공")
	void 토큰에서_유저_식별자_추출_성공 () throws Exception {
	    // given
		final String secretKey = "oayMoi4jNAQl7rDN3cj20Kwu0zHMjqWWmkokUCKWHEp3vmzJqhiFu06htixZbOZyF50wERI6CZxIt1UlJxZM3kgrvuwNZ2Q6RcKniLFYZf8VmsT0Dg18Wj0UwPgceB7ss5lt5FPkQ9yS8EEb8QcRuIOcmYKkEr2cvK3n8f98OfK";
		injectValue(queueTokenExtractor, "secretKey", secretKey);

		final Instant issuedAt = Instant.now();
		final Instant expiredAt = issuedAt.plusSeconds(300);

		final String token = Jwts.builder()
			.claim("userId", 1L)
			.issuedAt(Date.from(issuedAt))
			.expiration(Date.from(expiredAt))
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();

		// when, then
		assertThat(queueTokenExtractor.extractUserIdFromToken(token)).isNotNull();
	}

	@Test
	@DisplayName("토큰 만료되면 유저 식별자 추출 실패")
	void 토큰_만료되면_유저_식별자_추출_실패 () throws Exception {
	    // given
		final String secretKey = "oayMoi4jNAQl7rDN3cj20Kwu0zHMjqWWmkokUCKWHEp3vmzJqhiFu06htixZbOZyF50wERI6CZxIt1UlJxZM3kgrvuwNZ2Q6RcKniLFYZf8VmsT0Dg18Wj0UwPgceB7ss5lt5FPkQ9yS8EEb8QcRuIOcmYKkEr2cvK3n8f98OfK";
		injectValue(queueTokenExtractor, "secretKey", secretKey);

		final Instant issuedAt = Instant.now().minusSeconds(300);
		final Instant expiredAt = issuedAt.minusSeconds(300);

		final String token = Jwts.builder()
			.claim("userId", 1L)
			.issuedAt(Date.from(issuedAt))
			.expiration(Date.from(expiredAt))
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();

		// when, then
		assertThatThrownBy(() -> queueTokenExtractor.extractUserIdFromToken(token))
			.isInstanceOf(ExpiredJwtException.class);
	}

	@Test
	@DisplayName("키가 일치하지 않으면 유저 식별자 추출 실패")
	void 키가_일치하지_않으면_유저_식별자_추출_실패 () throws Exception {
	    // given
		final String secretKey = "oayMoi4jNAQl7rDN3cj20Kwu0zHMjqWWmkokUCKWHEp3vmzJqhiFu06htixZbOZyF50wERI6CZxIt1UlJxZM3kgrvuwNZ2Q6RcKniLFYZf8VmsT0Dg18Wj0UwPgceB7ss5lt5FPkQ9yS8EEb8QcRuIOcmYKkEr2cvK3n8f98OfK";
		injectValue(queueTokenExtractor, "secretKey", secretKey);

		final String otherSecretKey = "805afa823b6d4f55d690abf0e7726a112aebc464f9f83a27de0409eb39410b06";

		final Instant issuedAt = Instant.now().minusSeconds(300);
		final Instant expiredAt = issuedAt.minusSeconds(300);

		final String token = Jwts.builder()
			.claim("userId", 1L)
			.issuedAt(Date.from(issuedAt))
			.expiration(Date.from(expiredAt))
			.signWith(Keys.hmacShaKeyFor(otherSecretKey.getBytes()))
			.compact();

		// when, then
		assertThatThrownBy(() -> queueTokenExtractor.extractUserIdFromToken(token))
			.isInstanceOf(SignatureException.class);
	}

	private void injectValue(final Object target, final String fieldName, final Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
}
