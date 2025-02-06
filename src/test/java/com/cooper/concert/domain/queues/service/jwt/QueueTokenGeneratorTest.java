package com.cooper.concert.domain.queues.service.jwt;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.security.WeakKeyException;

import com.cooper.concert.domain.queues.infrastructure.jwt.JwtQueueTokenGenerator;

class QueueTokenGeneratorTest {

	private JwtQueueTokenGenerator queueTokenGenerator;

	@BeforeEach
	public void setUp() {
		queueTokenGenerator = new JwtQueueTokenGenerator();
	}

	@Test
	@DisplayName("JWT 생성 성공")
	public void JWT_생성_성공() throws Exception {
		// given
		Long userId = 1L;
		Instant issuedAt = Instant.now();
		injectValue(queueTokenGenerator, "secretKey",
			"oayMoi4jNAQl7rDN3cj20Kwu0zHMjqWWmkokUCKWHEp3vmzJqhiFu06htixZbOZyF50wERI6CZxIt1UlJxZM3kgrvuwNZ2Q6RcKniLFYZf8VmsT0Dg18Wj0UwPgceB7ss5lt5FPkQ9yS8EEb8QcRuIOcmYKkEr2cvK3n8f98OfK");
		injectValue(queueTokenGenerator, "validSeconds", 3600L);

		// when
		String sut = queueTokenGenerator.generateJwt(userId, issuedAt);

		// then
		assertThat(sut).isNotNull();
	}

	@Test
	@DisplayName("secret key 길이가 256이 아닐 경우 실패")
	public void secret_key_길이가_256이_아닐_경우_실패() throws Exception {
		// given
		Long userId = 1L;
		Instant issuedAt = Instant.now();
		injectValue(queueTokenGenerator, "secretKey", "asdf");
		injectValue(queueTokenGenerator, "validSeconds", 3600L);

		// when, then
		assertThatThrownBy(() -> queueTokenGenerator.generateJwt(userId, issuedAt))
			.isInstanceOf(WeakKeyException.class);
	}

	private void injectValue(final Object target, final String fieldName, final Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
}
