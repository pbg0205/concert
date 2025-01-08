package com.cooper.concert.interfaces.api.reservations.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cooper.concert.domain.reservations.service.dto.response.ConcertScheduleResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertScheduleReadUseCase;

@WebMvcTest(value = ConcertScheduleController.class)
class ConcertScheduleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ConcertScheduleReadUseCase concertScheduleReadUseCase;

	@Test
	@DisplayName("콘서트가 존재하지 않을 경우 요청 실패")
	void 콘서트가_존재하지_않을_경우_요청_실패() throws Exception {
		// given
		final Long concertId = 1000L;
		final int page = 1;

		when(concertScheduleReadUseCase.readAllScheduleByConcertIdAndPaging(anyLong(), anyInt(), anyInt()))
			.thenThrow(new ConcertNotFoundException(ConcertErrorType.CONCERT_NOT_FOUND));

		// when
		final ResultActions result = mockMvc.perform(
			get("/api/concert/{concertId}/available-dates?page={page}", concertId, page)
				.header("QUEUE-TOKEN", "queue-token")
				.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_CONCERT03"),
				jsonPath("$.error.message").value("콘서트를 조회할 수 없습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("콘서트가 존재하는 경우 요청 성공")
	void 콘서트가_존재할_경우_요청_성공() throws Exception {
		// given
		final Long concertId = 1000L;
		final int page = 1;

		when(concertScheduleReadUseCase.readAllScheduleByConcertIdAndPaging(anyLong(), anyInt(), anyInt()))
			.thenReturn(List.of(
					new ConcertScheduleResult(1L, LocalDateTime.of(2025, 1, 8, 0, 0, 0)),
					new ConcertScheduleResult(2L, LocalDateTime.of(2025, 1, 9, 0, 0, 0)),
					new ConcertScheduleResult(3L, LocalDateTime.of(2025, 1, 10, 0, 0, 0)),
					new ConcertScheduleResult(4L, LocalDateTime.of(2025, 1, 11, 0, 0, 0)),
					new ConcertScheduleResult(5L, LocalDateTime.of(2025, 1, 12, 0, 0, 0)),
					new ConcertScheduleResult(6L, LocalDateTime.of(2025, 1, 13, 0, 0, 0)),
					new ConcertScheduleResult(7L, LocalDateTime.of(2025, 1, 14, 0, 0, 0)),
					new ConcertScheduleResult(8L, LocalDateTime.of(2025, 1, 15, 0, 0, 0)),
					new ConcertScheduleResult(9L, LocalDateTime.of(2025, 1, 16, 0, 0, 0)),
					new ConcertScheduleResult(10L, LocalDateTime.of(2025, 1, 17, 0, 0, 0))
				)
			);

		// when
		final ResultActions result = mockMvc.perform(
			get("/api/concert/{concertId}/available-dates?page={page}", concertId, page)
				.header("QUEUE-TOKEN", "queue-token")
				.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$.result").value("SUCCESS"),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(print());
	}
}
