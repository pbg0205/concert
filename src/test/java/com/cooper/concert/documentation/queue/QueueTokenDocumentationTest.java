package com.cooper.concert.documentation.queue;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;

import com.cooper.concert.api.support.response.ResultType;
import com.cooper.concert.documentation.RestDocsDocumentationTest;
import com.cooper.concert.interfaces.api.queues.dto.request.QueueTokenIssueRequest;

class QueueTokenDocumentationTest extends RestDocsDocumentationTest {

	@Test
	@Sql("classpath:sql/queue_token_issue_integration.sql")
	void 대기열_토큰_발급_성공() throws Exception {
		// given
		final UUID userId = UUID.fromString("01943b62-8fed-7ea1-9d56-085529e28b11");
		final QueueTokenIssueRequest userBalanceReChargeRequest = new QueueTokenIssueRequest(userId);

		final String requestBody = objectMapper.writeValueAsString(userBalanceReChargeRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/queue/token/issue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("token-generate-success", userBalanceRechargeSuccessResource()));
	}

	private ResourceSnippet userBalanceRechargeSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Queue Token API")
				.summary("대기열 토큰 API")
				.description("대기열의 토큰을 발급한다.")
				.requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.token").type(JsonFieldType.STRING).description("대기열 토큰"),
					fieldWithPath("data.position").type(JsonFieldType.NUMBER).description("대기열 순서"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
