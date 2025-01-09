package com.cooper.concert.domain.reservations.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertSeatNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatQueryRepository;

@ExtendWith(MockitoExtension.class)
class ConcertSeatReadServiceTest {

	@InjectMocks
	private ConcertSeatReadService concertSeatReadService;

	@Mock
	private ConcertSeatQueryRepository concertSeatQueryRepository;

	@Test
	@DisplayName("콘서트 좌석이 없는 경우 ConcertSeatNotFoundException 반환")
	void 콘서트_좌석이_없는_경우_예외_반환() {
		// given
		when(concertSeatQueryRepository.findConcertSeatPriceInfoById(anyLong())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> concertSeatReadService.findConcertSeatReadInfoById(1L))
			.isInstanceOf(ConcertSeatNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> {
				assertThat((ConcertErrorType)errorType).isEqualTo(ConcertErrorType.CONCERT_SEAT_NOT_FOUND);
			});
	}

	@Test
	@DisplayName("콘서트 좌석이 있는 경우 성공")
	void 콘서트_좌석이_있는_경우_성공() {
		// given
		when(concertSeatQueryRepository.findConcertSeatPriceInfoById(anyLong()))
			.thenReturn(new ConcertSeatPriceInfo(1L, 1L, 3000L));

		// when
		final ConcertSeatPriceInfo concertSeatPriceInfo = concertSeatReadService.findConcertSeatReadInfoById(1L);

		// then
		assertThat(concertSeatPriceInfo.price()).isEqualTo(3000L);
	}
}
