package com.cooper.concert.documentation.reservations;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;

import com.cooper.concert.common.api.support.response.ResultType;
import com.cooper.concert.documentation.RestDocsDocumentationTest;
import com.cooper.concert.interfaces.api.reservations.dto.request.ConcertReservationRequest;

@Sql("classpath:sql/concert_reservation_sample_integration.sql")
class ConcertReservationDocumentationTest extends RestDocsDocumentationTest {

	@Test
	void 예약_요청() throws Exception {
		// given
		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(1L);

		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
			.header("QUEUE-TOKEN", "01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("concert-reservation-success", paymentSuccessResource()));
	}

	private ResourceSnippet paymentSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Concert Reservation API")
				.summary("콘서트 예약 API")
				.description("콘서트 좌석을 예약한다.")
				.requestHeaders(
					headerWithName("QUEUE-TOKEN").description("대기열 토큰"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.reservationId").type(JsonFieldType.STRING).description("예약 아이디"),
					fieldWithPath("data.paymentId").type(JsonFieldType.STRING).description("결제 아이디"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
