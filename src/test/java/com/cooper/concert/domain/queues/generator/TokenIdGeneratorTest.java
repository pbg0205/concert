package com.cooper.concert.domain.queues.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.uuid.UUIDType;
import com.fasterxml.uuid.impl.UUIDUtil;

class TokenIdGeneratorTest {

	private TokenIdGenerator tokenIdGenerator;

	@BeforeEach
	void setUp() {
		this.tokenIdGenerator = new TokenIdGenerator();
	}

	@Test
	@DisplayName("대기열 식별자 발급 성공")
	void 대기열_식별자_발급_성공() {
		// given. when
		final UUID sut = tokenIdGenerator.generateTokenId();

		// then
		assertThat(UUIDUtil.typeOf(sut)).isEqualTo(UUIDType.TIME_BASED_EPOCH);
	}

}
