package com.cooper.concert.common.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cooper.concert.common.api.components.filter.HttpLoggingFilter;

@Configuration
public class WebFilterConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean<HttpLoggingFilter> loggingFilter(){
		FilterRegistrationBean<HttpLoggingFilter> registrationBean
			= new FilterRegistrationBean<>();

		registrationBean.setFilter(new HttpLoggingFilter());
		registrationBean.addUrlPatterns("/api/*");
		registrationBean.setOrder(1);

		return registrationBean;
	}
}
