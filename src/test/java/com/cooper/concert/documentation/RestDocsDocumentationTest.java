package com.cooper.concert.documentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.cooper.concert.base.listener.DataCleanUpExecutionListener;
import com.cooper.concert.domain.queues.service.dto.ActiveQueueToken;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenGenerator;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(value = {
	DataCleanUpExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class RestDocsDocumentationTest {

	@Value("${token.processing.key.prefix}")
	private String activeTokenKeyPrefix;

	@Value("${token.processing.valid.minutes}")
	private Integer expireValidMinutes;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected WebApplicationContext context;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	protected QueueTokenGenerator queueTokenGenerator;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(this.context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(restDocumentation)
				.operationPreprocessors()
				.withRequestDefaults(modifyUris().removePort(), prettyPrint())
				.withResponseDefaults(modifyUris().removePort(), prettyPrint()))
			.alwaysDo(print())
			.build();
	}

	@BeforeEach
	public void initData() {
		final Long userId = 1L;
		final String key = activeTokenKeyPrefix + userId;
		final ActiveQueueToken activeQueueToken =
			new ActiveQueueToken(userId, LocalDateTime.now().plusMinutes(expireValidMinutes));

		redisTemplate.opsForHash().put(activeTokenKeyPrefix, userId, activeQueueToken);
	}

	@AfterEach
	void tearDown() {
		redisTemplate.delete("*");
	}
}
