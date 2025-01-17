package com.cooper.concert.api.components.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.UUID;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.api.components.filter.request.HttpRequestBodyCachedWrapper;
import com.cooper.concert.api.components.filter.response.HttpResponseBodyCachedWrapper;

@Slf4j
public class HttpLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpRequestBodyCachedWrapper requestWrapper =
			new HttpRequestBodyCachedWrapper((HttpServletRequest)request);
		HttpResponseBodyCachedWrapper responseWrapper =
			new HttpResponseBodyCachedWrapper((HttpServletResponse)response);

		logRequest(requestWrapper);

		chain.doFilter(requestWrapper, responseWrapper);

		logResponse(responseWrapper);
		writeResponseBody(responseWrapper, response);
	}

	private void logRequest(HttpRequestBodyCachedWrapper request) throws IOException {
		String url = request.getRequestURI();
		String method = request.getMethod();
		String headers = getRequestHeadersAsString(request);
		String body = getRequestBodyAsString(request);

		log.info("HTTP Request - URL: {}, Method: {}, Headers: {}, Body: {}", url, method, headers, body);
	}

	private String getRequestHeadersAsString(HttpServletRequest request) {
		final StringBuilder headers = new StringBuilder();
		final Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headers.append(headerName).append(": ").append(headerValue).append("; ");
		}
		return headers.toString().trim();
	}

	private String getRequestBodyAsString(final HttpRequestBodyCachedWrapper request) throws IOException {
		return new String(request.getCachedBody(),
			request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8");
	}

	private void logResponse(final HttpResponseBodyCachedWrapper response) throws IOException {
		final int status = response.getStatus();
		final String headers = getResponseHeadersAsString(response);
		final String body = new String(response.getCachedBody(),
			(response.getCharacterEncoding() != null) ? response.getCharacterEncoding() : "UTF-8");

		log.info("HTTP Response - Status: {}, Headers: {}, Body: {}", status, headers, body);
	}

	private String getResponseHeadersAsString(HttpServletResponse response) {
		final StringBuilder headers = new StringBuilder();
		final Collection<String> headerNames = response.getHeaderNames();

		for (String headerName : headerNames) {
			String headerValue = response.getHeader(headerName);
			headers.append(headerName).append(": ").append(headerValue).append("; ");
		}
		return headers.toString().trim();
	}

	// 우회한 response body byte 코드 주입해야 하므로 제거 금지!!
	private void writeResponseBody(HttpResponseBodyCachedWrapper cachedResponse, ServletResponse response)
		throws IOException {
		final byte[] responseBody = cachedResponse.getCachedBody();
		response.getOutputStream().write(responseBody);
	}
}
