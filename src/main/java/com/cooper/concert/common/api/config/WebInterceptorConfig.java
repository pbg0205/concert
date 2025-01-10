package com.cooper.concert.common.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.api.components.interceptor.QueueTokenValidationInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebInterceptorConfig implements WebMvcConfigurer {

	private final QueueTokenValidationInterceptor queueTokenValidationInterceptor;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(queueTokenValidationInterceptor).addPathPatterns("/api/concert/**", "/api/payments/**");
	}
}
