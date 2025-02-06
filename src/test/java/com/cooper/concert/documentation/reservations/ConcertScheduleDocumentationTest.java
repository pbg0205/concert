package com.cooper.concert.documentation.reservations;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

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

class ConcertScheduleDocumentationTest extends RestDocsDocumentationTest {

	@Test
	@Sql({"classpath:sql/integration/concert_schedule_available_dates_integration.sql"})
	void 예약_가능_날짜_조회_성공() throws Exception {
		// given
		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());

		// when
		final ResultActions result = mockMvc.perform(get("/api/concert/{concertId}/available-dates?page={page}", 1, 1)
			.header("QUEUE-TOKEN", token)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("concert-available-dates-success", concertAvailableDatesSuccessResource()));
	}

	private ResourceSnippet concertAvailableDatesSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Concert Schedule API")
				.summary("콘서트 예약 가능 날짜 조회 API")
				.description("콘서트 예약 가능 날짜를 조회한다.")
				.requestHeaders(
					headerWithName("QUEUE-TOKEN").description("대기열 토큰"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.pathParameters(
					parameterWithName("concertId").description("콘서트 아이디"))
				.queryParameters(
					parameterWithName("page").description("페이지")
				)
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.[0].concertScheduleId").type(JsonFieldType.NUMBER).description("콘서트 스케줄 아이디"),
					fieldWithPath("data.[0].availableDate").type(JsonFieldType.STRING).description("콘서트 스케줄 날짜"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}

	@Test
	@Sql({"classpath:sql/integration/concert_available_seats_integration.sql"})
	void 예약_가능_날짜_좌석_조회_성공() throws Exception {
		// given
		final String token = queueTokenGenerator.generateJwt(1L, Instant.now());

		// when
		final ResultActions result = mockMvc.perform(
			get("/api/concert/{concertScheduleId}/seats?page={page}", 1L, 1)
				.header("QUEUE-TOKEN", token)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().is2xxSuccessful(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("concert-available-date-seats-success", concertAvailableDateSeatsSuccessResource()));
	}

	private ResourceSnippet concertAvailableDateSeatsSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Concert Schedule API")
				.summary("콘서트 예약 가능 날짜 좌석 조회 API")
				.description("콘서트 예약 가능 날짜의 좌석을 조회한다.")
				.requestHeaders(
					headerWithName("QUEUE-TOKEN").description("대기열 토큰"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"))
				.pathParameters(
					parameterWithName("concertScheduleId").description("콘서트 스케줄 아이디")
				)
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.date").type(JsonFieldType.STRING).description("예약 가능 날짜"),
					fieldWithPath("data.availableSeats[0].id").type(JsonFieldType.NUMBER).description("예약 가능 좌석 아이디"),
					fieldWithPath("data.availableSeats[0].seatNumber").type(JsonFieldType.NUMBER).description("예약 가능 좌석 번호"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build()
		);
	}
}
