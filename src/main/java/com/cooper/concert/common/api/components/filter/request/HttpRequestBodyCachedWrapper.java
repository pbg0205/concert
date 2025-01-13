package com.cooper.concert.common.api.components.filter.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;

@Getter
public class HttpRequestBodyCachedWrapper extends HttpServletRequestWrapper {

	private final byte[] cachedBody;

	public HttpRequestBodyCachedWrapper(HttpServletRequest request) throws IOException {
		super(request);
		final InputStream requestInputStream = request.getInputStream();
		this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new RequestBodyCachedServletInputStream(this.cachedBody);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
		return new BufferedReader(new InputStreamReader(byteArrayInputStream));
	}
}

