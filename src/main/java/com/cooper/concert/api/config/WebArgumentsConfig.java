package com.cooper.concert.api.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.components.resolver.QueueTokenArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebArgumentsConfig implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new QueueTokenArgumentResolver());
	}
}
