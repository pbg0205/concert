package com.cooper.concert.interfaces.api.payments.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.cooper.concert.common.api.config.WebConfig;
import com.cooper.concert.domain.payments.service.dto.response.PaymentProcessResult;
import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentCompleteFailException;
import com.cooper.concert.domain.reservations.service.errors.ReservationErrorType;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationCanceledException;
import com.cooper.concert.domain.reservations.service.errors.exception.ReservationReservedException;
import com.cooper.concert.interfaces.api.payments.dto.request.PaymentProcessRequest;
import com.cooper.concert.interfaces.api.payments.usecase.PaymentProcessUseCase;
import com.cooper.concert.interfaces.api.queues.interceptor.QueueTokenValidationInterceptor;

@WebMvcTest(value = PaymentProcessController.class, excludeFilters = {@ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, QueueTokenValidationInterceptor.class})})
class PaymentProcessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private PaymentProcessUseCase paymentProcessUseCase;

	@Test
	@DisplayName("결제와 예약 모두 대기 상태인 경우 결제 성공")
	void 결제_성공() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");
		final UUID reservationId = UUID.fromString("01944c18-83d4-7969-b03e-4157c54a4249");

		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		when(paymentProcessUseCase.processPayment(any()))
			.thenReturn(new PaymentProcessResult(reservationId));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isOk(),
			jsonPath("$.result").value("SUCCESS"),
			jsonPath("$.data.reservationId").value(reservationId.toString()),
			jsonPath("$.error").doesNotExist());
	}

	@Test
	@DisplayName("예약 취소 상태에서 재요청하면 요청 실패")
	void 예약_취소_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");

		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		when(paymentProcessUseCase.processPayment(any()))
			.thenThrow(new ReservationCanceledException(ReservationErrorType.RESERVATION_CANCELED));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_RESERVATION02"),
			jsonPath("$.error.message").value("해당 예약은 취소 되었습니다."));
	}

	@Test
	@DisplayName("예약 성공 상태에서 재요청하면 요청 실패")
	void 예약_성공_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");

		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		when(paymentProcessUseCase.processPayment(any()))
			.thenThrow(new ReservationReservedException(ReservationErrorType.RESERVATION_RESERVED));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_RESERVATION03"),
			jsonPath("$.error.message").value("해당 예약은 이미 예약 되었습니다."));
	}

	@Test
	@DisplayName("결제 취소 상태에서 재요청하면 요청 실패")
	void 결제_취소_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");

		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		when(paymentProcessUseCase.processPayment(any()))
			.thenThrow(new PaymentCompleteFailException(PaymentErrorType.PAYMENT_CANCELED));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_PAYMENT02"),
			jsonPath("$.error.message").value("결제가 취소된 상태 입니다."));
	}
	@Test
	@DisplayName("결제 완료 상태에서 재요청하면 요청 실패")
	void 결제_완료_상태에서_재요청하면_요청_실패() throws Exception {
		// given
		final UUID paymentId = UUID.fromString("01944bfc-3939-7e31-add2-2f356099b3b3");

		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);
		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		when(paymentProcessUseCase.processPayment(any()))
			.thenThrow(new PaymentCompleteFailException(PaymentErrorType.PAYMENT_COMPLETED));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/payments")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_PAYMENT03"),
			jsonPath("$.error.message").value("결제가 이미 완료된 상태입니다."));
	}
}
