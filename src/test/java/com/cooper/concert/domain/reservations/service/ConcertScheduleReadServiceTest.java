package com.cooper.concert.domain.reservations.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleSeatsResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.exception.ConcertScheduleNotFoundException;
import com.cooper.concert.domain.reservations.service.repository.ConcertQueryRepository;
import com.cooper.concert.domain.reservations.service.repository.ConcertScheduleQueryRepository;
import com.cooper.concert.domain.reservations.service.repository.ConcertSeatQueryRepository;

@ExtendWith(MockitoExtension.class)
class ConcertScheduleReadServiceTest {

	@InjectMocks
	private ConcertScheduleReadService concertScheduleReadService;

	@Mock
	private ConcertScheduleQueryRepository concertScheduleQueryRepository;

	@Mock
	private ConcertQueryRepository concertQueryRepository;

	@Mock
	private ConcertSeatQueryRepository concertSeatQueryRepository;

	@Test
	@DisplayName("콘서트가 존재하지 않으면 콘서트 일정 조회 실패")
	void 콘서트가_존재하지_않으면_콘서트_일정_조회_실패() {
		// given
		final Long concertId = 1L;

		when(concertQueryRepository.existsById(any())).thenReturn(false);

		// when, then
		assertThatThrownBy(
			() -> concertScheduleReadService.findByAllByConcertIdAndPaging(concertId, 1, 10))
			.isInstanceOf(ConcertNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType -> assertThat(((ConcertErrorType)errorType))
				.isEqualTo(ConcertErrorType.CONCERT_NOT_FOUND));
	}

	@Test
	@DisplayName("콘서트가 존재하면 콘서트 일정 조회 성공")
	void 콘서트가_존재하면_콘서트_일정_조회_성공() {
		// given
		final Long concertId = 1L;

		when(concertQueryRepository.existsById(any())).thenReturn(true);
		when(concertScheduleQueryRepository.findByAllByConcertIdAndPaging(any(), anyInt(), anyInt()))
			.thenReturn(List.of(
				new ConcertScheduleResult(1L, LocalDateTime.of(2025, 1, 10, 18, 0)),
				new ConcertScheduleResult(2L, LocalDateTime.of(2025, 1, 11, 18, 0)),
				new ConcertScheduleResult(3L, LocalDateTime.of(2025, 1, 12, 18, 0)),
				new ConcertScheduleResult(4L, LocalDateTime.of(2025, 1, 13, 18, 0)),
				new ConcertScheduleResult(5L, LocalDateTime.of(2025, 1, 14, 18, 0)),
				new ConcertScheduleResult(6L, LocalDateTime.of(2025, 1, 15, 18, 0)),
				new ConcertScheduleResult(7L, LocalDateTime.of(2025, 1, 16, 18, 0)),
				new ConcertScheduleResult(8L, LocalDateTime.of(2025, 1, 17, 18, 0)),
				new ConcertScheduleResult(9L, LocalDateTime.of(2025, 1, 18, 18, 0)),
				new ConcertScheduleResult(10L, LocalDateTime.of(2025, 1, 19, 18, 0))
			));

		// when
		final List<ConcertScheduleResult> sut =
			concertScheduleReadService.findByAllByConcertIdAndPaging(concertId, 1, 10);

		// then
		assertThat(sut).hasSize(10);
	}

	@Test
	@DisplayName("콘서트 일정 없으면 좌석 조회 실패")
	void 콘서트_일정_없으면_좌석_조회_실패() {
		// given
		final Long scheduleId = 1L;
		final Integer offset = 0;
		final Integer limit = 10;

		when(concertScheduleQueryRepository.findConcertScheduleResultById(any()))
			.thenThrow(new ConcertScheduleNotFoundException(ConcertErrorType.CONCERT_SCHEDULE_NOT_FOUND));

		// when, then
		assertThatThrownBy(
			() -> concertScheduleReadService.findAvailableSeatsByScheduleIdAndPaging(scheduleId, offset, limit))
			.isInstanceOf(ConcertScheduleNotFoundException.class)
			.extracting("errorType")
			.satisfies(errorType ->
				assertThat(((ConcertErrorType)errorType)).isEqualTo(ConcertErrorType.CONCERT_SCHEDULE_NOT_FOUND));

	}

	@Test
	@DisplayName("콘서트 일정 있으면 좌석 조회 성공")
	void 콘서트_일정_있으면_좌석_조회_성공() {
		// given
		final Long scheduleId = 1L;
		final Integer offset = 0;
		final Integer limit = 10;
		final List<Long> availableSeats = List.of(1L, 3L, 5L, 10L);

		when(concertScheduleQueryRepository.findConcertScheduleResultById(any()))
			.thenReturn(new ConcertScheduleResult(1L, LocalDateTime.of(2025, 1, 9, 2, 30)));
		when(
			concertSeatQueryRepository.findConcertSeatsByScheduleIdAndStatusAndPaging(any(), any(), anyInt(), anyInt()))
			.thenReturn(availableSeats);

		// when
		final ConcertScheduleSeatsResult sut =
			concertScheduleReadService.findAvailableSeatsByScheduleIdAndPaging(scheduleId, offset, limit);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(sut.date()).isEqualTo(LocalDate.of(2025, 1, 9));
			softAssertions.assertThat(sut.availableSeats()).hasSize(4);
		});
	}
}
