package com.cooper.concert.documentation.reservations;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.cooper.concert.reservations.presentation.dto.request.ConcertReservationRequest;

class ConcertReservationDocumentationTest extends RestDocsDocumentationTest {

	@Test
	void 예약_요청() throws Exception {
		// given
		final ConcertReservationRequest concertReservationRequest = new ConcertReservationRequest(
			UUID.randomUUID(),
			LocalDate.of(2025, 1, 5),
			3);

		final String requestBody = objectMapper.writeValueAsString(concertReservationRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/concert/seats/reservation")
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
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.reservationId").type(JsonFieldType.STRING).description("예약 아이디"),
					fieldWithPath("data.paymentId").type(JsonFieldType.STRING).description("결제 아이디"),
					fieldWithPath("data.date").type(JsonFieldType.STRING).description("콘서트 날짜"),
					fieldWithPath("data.seat").type(JsonFieldType.NUMBER).description("콘서트 좌석"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
