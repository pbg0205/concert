package com.cooper.concert.domain.queues.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.ExpiredQueueTokenException;

class JwtQueueTokenExtractorTest {

	private JwtQueueTokenExtractor queueTokenExtractor;

	@BeforeEach
	void setUp() throws NoSuchFieldException, IllegalAccessException {
		this.queueTokenExtractor = new JwtQueueTokenExtractor();
		initValue();
	}

	@Test
	@DisplayName("만료 토큰인 경우, 만료 예외 반환")
	void 만료_토큰인_경우_만료_예외_반환 () throws NoSuchFieldException {
	    // given
		final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcwOTU4MjQwMH0.KXewVF1-wLm2xlhnW0w6ynk9fT0gWFakDKwvtEKDbo4";

		// when, then
		assertThatThrownBy(() -> queueTokenExtractor.extractUserIdFromToken(token))
			.isInstanceOf(ExpiredQueueTokenException.class)
			.extracting("errorType").isEqualTo(TokenErrorType.TOKEN_EXPIRED);
	}

	private void initValue() throws NoSuchFieldException, IllegalAccessException {
		final Field secretKeyField = JwtQueueTokenExtractor.class.getDeclaredField("secretKey");
		secretKeyField.setAccessible(true);
		secretKeyField.set(queueTokenExtractor, "327b11e16b3095aecfebb79d1253a111f54aa2f98a373ed21cf2ff9f9f42c71b");
	}

}
