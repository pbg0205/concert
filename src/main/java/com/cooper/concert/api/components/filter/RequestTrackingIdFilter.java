package com.cooper.concert.api.components.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class RequestTrackingIdFilter  implements Filter {

	private static final String REQUEST_ID = "REQUEST-ID";

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws
		IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String requestId = UUID.randomUUID().toString();
		MDC.put(REQUEST_ID, requestId);

		try {
			httpResponse.addHeader(REQUEST_ID, requestId);
			chain.doFilter(request, response);
		} finally {
			MDC.remove(REQUEST_ID);
		}
	}
}
