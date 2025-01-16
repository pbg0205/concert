package com.cooper.concert.api.components.filter.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

public class RequestBodyCachedServletInputStream extends ServletInputStream {

	private final InputStream cachedBodyInputStream;

	public RequestBodyCachedServletInputStream(byte[] cachedBody) {
		this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
	}

	@Override
	public boolean isFinished() {
		try {
			return cachedBodyInputStream.available() == 0;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read() throws IOException {
		return cachedBodyInputStream.read();
	}
}
