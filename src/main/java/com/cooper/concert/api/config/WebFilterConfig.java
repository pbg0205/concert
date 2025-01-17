package com.cooper.concert.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cooper.concert.api.components.filter.HttpLoggingFilter;
import com.cooper.concert.api.components.filter.RequestTrackingIdFilter;

@Configuration
public class WebFilterConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean<RequestTrackingIdFilter> requestTrackingIdFilter(){
		FilterRegistrationBean<RequestTrackingIdFilter> registrationBean
			= new FilterRegistrationBean<>();

		registrationBean.setFilter(new RequestTrackingIdFilter());
		registrationBean.addUrlPatterns("/api/*");
		registrationBean.setOrder(1);

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<HttpLoggingFilter> loggingFilter(){
		FilterRegistrationBean<HttpLoggingFilter> registrationBean
			= new FilterRegistrationBean<>();

		registrationBean.setFilter(new HttpLoggingFilter());
		registrationBean.addUrlPatterns("/api/*");
		registrationBean.setOrder(2);

		return registrationBean;
	}
}
