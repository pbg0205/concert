package com.cooper.concert.common.api.components.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.common.api.components.filter.request.HttpRequestBodyCachedWrapper;

@Slf4j
public class HttpRequestLoggingFilter implements Filter {

	private static final String REQUEST_ID = "request-id";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		try {
			MDC.put(REQUEST_ID, UUID.randomUUID().toString());

			HttpRequestBodyCachedWrapper httpRequestBodyCachedWrapper =
				new HttpRequestBodyCachedWrapper((HttpServletRequest)request);

			logRequest(httpRequestBodyCachedWrapper);
			chain.doFilter(httpRequestBodyCachedWrapper, response);
		}finally {
			MDC.remove(REQUEST_ID);
		}
	}

	private void logRequest(HttpRequestBodyCachedWrapper request) throws IOException {
		String url = request.getRequestURI();
		String method = request.getMethod();
		String headers = getRequestHeadersAsString(request);
		String body = getRequestBodyAsString(request);

		log.info("HTTP Request - URL: {}, Method: {}, Headers: {}, Body: {}", url, method, headers, body);
	}

	private String getRequestHeadersAsString(HttpServletRequest request) {
		StringBuilder headers = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headers.append(headerName).append(": ").append(headerValue).append("; ");
		}
		return headers.toString().trim();
	}

	private String getRequestBodyAsString(HttpRequestBodyCachedWrapper request) throws IOException {
		return new String(request.getCachedBody(),
			request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8");
	}
}
