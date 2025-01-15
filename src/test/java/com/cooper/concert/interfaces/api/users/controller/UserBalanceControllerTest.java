package com.cooper.concert.interfaces.api.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

import com.cooper.concert.api.config.WebInterceptorConfig;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.InvalidUserPointException;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.domain.users.service.response.UserBalanceChargeResult;
import com.cooper.concert.domain.users.service.response.UserBalanceReadResult;
import com.cooper.concert.api.components.interceptor.QueueTokenValidationInterceptor;
import com.cooper.concert.interfaces.api.users.dto.request.UserBalanceReChargeRequest;
import com.cooper.concert.interfaces.api.users.usecase.UserBalanceChargeUseCase;
import com.cooper.concert.interfaces.api.users.usecase.UserBalanceReadUseCase;

@WebMvcTest(value = UserBalanceController.class, excludeFilters = {@ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE, classes = {WebInterceptorConfig.class, QueueTokenValidationInterceptor.class})})
class UserBalanceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private UserBalanceChargeUseCase userBalanceChargeUseCase;

	@MockitoBean
	private UserBalanceReadUseCase userBalanceReadUseCase;

	@Test
	@DisplayName("없는 사용자의 식별자인 경우, 사용자 포인트 충전 실패")
	void 없는_사용자_아이디_경우_포인트_충전_요청_실패() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");
		final Long point = 1000L;

		final UserBalanceReChargeRequest userBalanceReChargeRequest = new UserBalanceReChargeRequest(userId, point);
		final String requestBody = objectMapper.writeValueAsString(userBalanceReChargeRequest);

		when(userBalanceChargeUseCase.chargePoint(any(), any()))
			.thenThrow(new UserNotFoundException(UserErrorType.USER_NOT_FOUND));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/users/balance/recharge")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_USER04"),
				jsonPath("$.error.message").value("유저를 찾을 수 없습니다"))
			.andDo(print());
	}

	@Test
	@DisplayName("요청 포인트가 음수인 경우, 사용자 포인트 충전 실패")
	void 요청_포인트_음수인_경우_포인트_충전_요청_실패() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");
		final Long point = -1L;

		final UserBalanceReChargeRequest userBalanceReChargeRequest = new UserBalanceReChargeRequest(userId, point);
		final String requestBody = objectMapper.writeValueAsString(userBalanceReChargeRequest);

		when(userBalanceChargeUseCase.chargePoint(any(), any()))
			.thenThrow(new InvalidUserPointException(UserErrorType.CHARGING_POINT_NEGATIVE));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/users/balance/recharge")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_USER06"),
				jsonPath("$.error.message").value("충전 포인트는 음수일 수 없습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("사용자 포인트 충전 성공")
	void 포인트_충전_요청_성공() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");
		final Long point = 1000L;

		final UserBalanceReChargeRequest userBalanceReChargeRequest = new UserBalanceReChargeRequest(userId, point);
		final String requestBody = objectMapper.writeValueAsString(userBalanceReChargeRequest);

		when(userBalanceChargeUseCase.chargePoint(any(), any()))
			.thenReturn(new UserBalanceChargeResult(2000L));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/users/balance/recharge")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isOk(),
			jsonPath("$.result").value("SUCCESS"),
			jsonPath("$.data.balance").value(2000),
			jsonPath("$.error").doesNotExist()
		);
	}

	@Test
	@DisplayName("없는 사용자의 식별자인 경우, 사용자 포인트 조회 실패")
	void 없는_사용자_아이디_경우_포인트_조회_요청_실패() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");

		when(userBalanceReadUseCase.readUserBalance(any()))
			.thenThrow(new UserNotFoundException(UserErrorType.USER_NOT_FOUND));

		// when
		final ResultActions sut = mockMvc.perform(get("/api/users/{userId}/balance", userId)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		sut.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.result").value("ERROR"),
				jsonPath("$.data").doesNotExist(),
				jsonPath("$.error.code").value("ERROR_USER04"),
				jsonPath("$.error.message").value("유저를 찾을 수 없습니다"))
			.andDo(print());
	}

	@Test
	@DisplayName("사용자 포인트 조회 성공")
	void 포인트_조회_요청_성공() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");

		when(userBalanceReadUseCase.readUserBalance(any()))
			.thenReturn(new UserBalanceReadResult(2000L));

		// when
		final ResultActions sut = mockMvc.perform(get("/api/users/{userId}/balance", userId)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		sut.andExpectAll(
				status().isOk(),
				jsonPath("$.result").value("SUCCESS"),
				jsonPath("$.data.balance").value(2000),
				jsonPath("$.error").doesNotExist())
			.andDo(print());
	}
}
