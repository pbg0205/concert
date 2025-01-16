package com.cooper.concert.documentation.payments;

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
import com.cooper.concert.interfaces.api.payments.dto.request.PaymentProcessRequest;

class PaymentProcessDocumentationTest extends RestDocsDocumentationTest {

	@Test
	@Sql("classpath:sql/integration/payment_process_integration.sql")
	void 결제_성공() throws Exception {
		// given
		UUID paymentId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
		final PaymentProcessRequest paymentProcessRequest = new PaymentProcessRequest(paymentId);

		final String requestBody = objectMapper.writeValueAsString(paymentProcessRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/payments")
			.header("QUEUE-TOKEN", "01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("payment-success", paymentSuccessResource()));
	}

	private ResourceSnippet paymentSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Payments API")
				.summary("결제 API")
				.description("콘서트 예약 금액을 결제한다.")
				.requestHeaders(
					headerWithName("QUEUE-TOKEN").description("대기열 토큰"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.reservationId").type(JsonFieldType.STRING).description("예약 아이디"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
