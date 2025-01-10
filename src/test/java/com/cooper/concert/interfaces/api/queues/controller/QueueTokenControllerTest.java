package com.cooper.concert.interfaces.api.queues.controller;

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

import com.cooper.concert.common.api.config.WebInterceptorConfig;
import com.cooper.concert.domain.queues.service.dto.QueueTokenIssueResult;
import com.cooper.concert.domain.users.service.errors.UserErrorType;
import com.cooper.concert.domain.users.service.errors.exception.UserNotFoundException;
import com.cooper.concert.interfaces.api.queues.dto.request.QueueTokenIssueRequest;
import com.cooper.concert.interfaces.api.queues.interceptor.QueueTokenValidationInterceptor;
import com.cooper.concert.interfaces.api.queues.usecase.QueueTokenIssueUseCase;

@WebMvcTest(value = QueueTokenController.class, excludeFilters = {@ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE, classes = {WebInterceptorConfig.class, QueueTokenValidationInterceptor.class})})
class QueueTokenControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private QueueTokenIssueUseCase queueTokenIssueUseCase;

	@Test
	@DisplayName("유저가 존재하지 않으면 토큰 발급 실패")
	void 유저가_존재하지_않으면_토큰_발급_실패() throws Exception {
		// given
		final UUID userId = UUID.fromString("0194411f-8b49-7d07-8154-2db648d82990");

		final QueueTokenIssueRequest queueTokenIssueRequest = new QueueTokenIssueRequest(userId);
		final String requestBody = objectMapper.writeValueAsString(queueTokenIssueRequest);

		when(queueTokenIssueUseCase.issueQueueToken(any()))
			.thenThrow(new UserNotFoundException(UserErrorType.USER_NOT_FOUND));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/queue/token/issue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.result").value("ERROR"),
			jsonPath("$.data").doesNotExist(),
			jsonPath("$.error.code").value("ERROR_USER04"),
			jsonPath("$.error.message").value("유저를 찾을 수 없습니다")
		);
	}

	@Test
	@DisplayName("유저가 존재하면 토큰 발급 성공")
	void 유저가_존재하면_토큰_발급_성공() throws Exception {
		// given
		final UUID userId = UUID.fromString("0194411f-8b49-7d07-8154-2db648d82990");
		final UUID tokenId = UUID.fromString("01944127-f6ce-7130-9de8-1b39b67c24ce");
		final Long waitingPosition = 3L;

		final QueueTokenIssueRequest queueTokenIssueRequest = new QueueTokenIssueRequest(userId);
		final String requestBody = objectMapper.writeValueAsString(queueTokenIssueRequest);

		when(queueTokenIssueUseCase.issueQueueToken(any()))
			.thenReturn(new QueueTokenIssueResult(tokenId, waitingPosition));

		// when
		final ResultActions sut = mockMvc.perform(post("/api/queue/token/issue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		sut.andExpectAll(
			status().isOk(),
			jsonPath("$.result").value("SUCCESS"),
			jsonPath("$.data.token").value(tokenId.toString()),
			jsonPath("$.data.position").value(3)
		);
	}

}
