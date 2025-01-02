package com.cooper.concert.documentation.users;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;

import com.cooper.concert.common.api.support.response.ResultType;
import com.cooper.concert.documentation.RestDocsDocumentationTest;
import com.cooper.concert.users.presentation.dto.request.UserBalanceReChargeRequest;

public class UserBalanceDocumentationTest extends RestDocsDocumentationTest {

	@Test
	void 유저_잔액_조회_성공() throws Exception {
		// given, when
		final ResultActions result = mockMvc.perform(get("/api/users/{userId}/balance", UUID.randomUUID())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("balance-find-success", userBalanceLookupSuccessResource()));
	}

	private ResourceSnippet userBalanceLookupSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("User Balance API")
				.summary("유저 잔액 조회 API")
				.description("유저 포인트 잔액 조회한다.")
				.requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.pathParameters(
					parameterWithName("userId").description("유저 아이디(UUID)"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.balance").type(JsonFieldType.NUMBER).description("사용자 포인트 잔액"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}

	@Test
	void 유저_잔액_충전_성공() throws Exception {
		// given
		final UserBalanceReChargeRequest userBalanceReChargeRequest =
			new UserBalanceReChargeRequest(UUID.randomUUID(), 1000L);

		final String requestBody = objectMapper.writeValueAsString(userBalanceReChargeRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/users/balance/recharge")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("balance-recharge-success", userBalanceRechargeSuccessResource()));
	}

	private ResourceSnippet userBalanceRechargeSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("User Balance API")
				.summary("유저 충전 조회 API")
				.description("유저 잔액을 충전한다.")
				.requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.balance").type(JsonFieldType.NUMBER).description("사용자 포인트 잔액"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
