package com.cooper.concert.domain.queues.service;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@ExtendWith(MockitoExtension.class)
class ActiveTokenServiceTest {

	@InjectMocks
	private ActiveTokenService activeTokenService;

	@Mock
	private ActiveTokenRepository activeTokenRepository;

	@Test
	@DisplayName("여유 활성화 토큰 갯수 조회 성공")
	void 여유_활성화_토큰_갯수_조회_성공 () throws Exception {
	    // given
		when(activeTokenRepository.countActiveTokens()).thenReturn(3);
		injectValue(activeTokenService, "processingTokenCapacity", 30);

	    // when
		final Integer sut = activeTokenService.countRemainingActiveTokens();

		// then
		Assertions.assertThat(sut).isEqualTo(27);
	}


	private void injectValue(final Object target, final String fieldName, final Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

}
