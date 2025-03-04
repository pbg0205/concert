package com.cooper.concert.interfaces.api.payments.controller;

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

import com.cooper.concert.domain.payments.service.dto.response.PaymentProcessResult;
import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentCompleteFailException;
import com.cooper.concert.domain.queues.service.ActiveQueueTokenValidationService;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenGenerator;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;
import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationCanceledException;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationReservedException;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.InvalidUserPointException;
import com.cooper.concert.interfaces.api.payments.dto.request.PaymentProcessRequest;
import com.cooper.concert.interfaces.api.payments.usecase.PaymentProcessUseCase;

@WebMvcTest(value = PaymentProcessController.class,
	includeFilters = {@ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {QueueTokenExtractor.class, QueueTokenGenerator.class, ActiveQueueTokenValidationService.class})})
class PaymentProcessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private QueueTokenGenerator queueTokenGenerator;

	@MockitoBean
	private PaymentProcessUseCase paymentProcessUseCase;

	@MockitoBean
	private ActiveTokenRepository activeTokenRepository;

	@Test
	@DisplayName("토큰 헤더가 없으면 요청 실패")
	void 토큰_헤더가_없으면_요청_실패() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/payments")
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
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);
		final String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcwOTU4MjQwMH0.KXewVF1-wLm2xlhnW0w6ynk9fT0gWFakDKwvtEKDbo4";

		// when
		final ResultActions result = mockMvc.perform(post("/api/payments")
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
	@DisplayName("결제와 예약 모두 대기 상태인 경우 결제 성공")
	void 결제_성공() throws Exception {
		// given
		final UUID reservationId = UUID.fromString("01944c18-83d4-7969-b03e-4157c54a4249");
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenReturn(new PaymentProcessResult(reservationId));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);


		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isOk(),
				jsonPath("$.result").value("SUCCESS"),
				jsonPath("$.data.reservationId").value(reservationId.toString()),
				jsonPath("$.error").doesNotExist())
			.andDo(print());
	}

	@Test
	@DisplayName("예약 취소 상태에서 재요청하면 요청 실패")
	void 예약_취소_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new ReservationCanceledException(ReservationErrorType.RESERVATION_CANCELED));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_RESERVATION02"),
				jsonPath("$.error.message").value("해당 예약은 취소 되었습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("예약 성공 상태에서 재요청하면 요청 실패")
	void 예약_성공_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new ReservationReservedException(ReservationErrorType.RESERVATION_RESERVED));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);


		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_RESERVATION03"),
				jsonPath("$.error.message").value("해당 예약은 이미 예약 되었습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("결제 취소 상태에서 재요청하면 요청 실패")
	void 결제_취소_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new PaymentCompleteFailException(PaymentErrorType.PAYMENT_CANCELED));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_PAYMENT02"),
			jsonPath("$.error.message").value("결제가 취소된 상태 입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("결제 완료 상태에서 재요청하면 요청 실패")
	void 결제_완료_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new PaymentCompleteFailException(PaymentErrorType.PAYMENT_COMPLETED));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);


		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_PAYMENT03"),
				jsonPath("$.error.message").value("결제가 이미 완료된 상태입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("결제키를 찾지 못하면 요청 실패")
	void 결제키를_찾지_못하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new PaymentCompleteFailException(PaymentErrorType.PAYMENT_KEY_NOT_FOUND));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);


		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_PAYMENT01"),
				jsonPath("$.error.message").value("결제 키를 찾을 수 없습니다"))
			.andDo(print());
	}

	@Test
	@DisplayName("유저 포인트 부족하면 요청 실패")
	void 유저_포인트_부족하면_요청_실패() throws Exception {
		// given
		when(paymentProcessUseCase.processPayment(any(), any()))
			.thenThrow(new InvalidUserPointException(UserErrorType.INSUFFICIENT_BALANCE));

		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());
		when(activeTokenRepository.existsByUserId(any())).thenReturn(true);

		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);


		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.header("QUEUE-TOKEN", token)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_USER07"),
				jsonPath("$.error.message").value("유저 잔고가 부족합니다."))
			.andDo(print());
	}
}
