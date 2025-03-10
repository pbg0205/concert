package com.cooper.concert.interfaces.api.reservations.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.cooper.concert.domain.queues.service.ActiveQueueTokenValidationService;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenGenerator;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationResult;
import com.cooper.concert.domain.reservations.service.errors.ConcertErrorType;
import com.cooper.concert.domain.reservations.service.errors.ConcertNotFoundException;
import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationUnavailableException;
import com.cooper.concert.interfaces.api.reservations.dto.request.ConcertReservationRequest;
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertReservationUseCase;

@WebMvcTest(value = ConcertReservationController.class,
	includeFilters = {@ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {QueueTokenExtractor.class, QueueTokenGenerator.class, ActiveQueueTokenValidationService.class})})
class ConcertReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private QueueTokenGenerator queueTokenGenerator;

	@MockitoBean
	private ConcertReservationUseCase concertReservationUseCase;

	@MockitoBean
	private ActiveTokenRepository activeTokenRepository;

	@Test
	@DisplayName("올바르지 않는 토큰인 경우 요청 실패")
	void 올바르지_않는_토큰인_경우_요청_실패() throws Exception {
		// given
		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", "invalid token")
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isForbidden(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_TOKEN03"),
				jsonPath("$.error.message").value("올바른 형식의 토큰 아닙니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("토큰 헤더가 없으면 요청 실패")
	void 토큰_헤더가_없으면_요청_실패() throws Exception {
		// given
		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_TOKEN04"),
				jsonPath("$.error.message").value("토큰이 비어 있습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("만료 토큰인 경우 요청 실패")
	void 만료_토큰인_경우_요청_실패() throws Exception {
		// given
		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);
		final String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcwOTU4MjQwMH0.KXewVF1-wLm2xlhnW0w6ynk9fT0gWFakDKwvtEKDbo4";

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", expiredToken)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isForbidden(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_TOKEN05"),
				jsonPath("$.error.message").value("만료된 토큰 입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("콘서트 좌석이 존재하지 않을 경우 요청 실패")
	void 콘서트_좌석이_존재하지_않을_경우_요청_실패() throws Exception {
		// given
		when(concertReservationUseCase.reserveConcertSeat(any(), any()))
			.thenThrow(new ConcertNotFoundException(ConcertErrorType.CONCERT_SEAT_NOT_FOUND));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.header("QUEUE-TOKEN", token)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_CONCERT05"),
				jsonPath("$.error.message").value("콘서트 좌석을 조회할 수 없습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("좌석이 점유된 경우 요청 실패")
	void 좌석이_점유된_경우_요청_실패() throws Exception {
		// given
		when(concertReservationUseCase.reserveConcertSeat(any(), any()))
			.thenThrow(new ReservationUnavailableException(ReservationErrorType.CONCERT_SEAT_OCCUPIED));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.header("QUEUE-TOKEN", token)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_RESERVATION01"),
				jsonPath("$.error.message").value("해당 좌석은 점유되어 있습니다"))
			.andDo(print());
	}

	@Test
	@DisplayName("좌석 예약 성공")
	void 좌석_예약_성공() throws Exception {
		// given
		final UUID reservationAltId = UUID.fromString("01944ab8-9243-709d-b15d-4bc69669f73b");
		final UUID paymentAltId = UUID.fromString("01944ab8-cd15-76f9-8c3e-245a4507f471");

		when(concertReservationUseCase.reserveConcertSeat(any(), any()))
			.thenReturn(new ConcertReservationResult(reservationAltId, paymentAltId));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);
		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.header("QUEUE-TOKEN", token)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$.result").value("SUCCESS"),
				jsonPath("$.data.reservationId").value(reservationAltId.toString()),
				jsonPath("$.data.paymentId").value(paymentAltId.toString()),
				jsonPath("$.error").doesNotExist())
			.andDo(print());
	}
}
