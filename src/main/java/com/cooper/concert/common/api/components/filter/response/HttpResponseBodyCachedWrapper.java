package com.cooper.concert.common.api.components.filter.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HttpResponseBodyCachedWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream cachedBody;
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public HttpResponseBodyCachedWrapper(HttpServletResponse response) {
        super(response);
        this.cachedBody = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called");
        }
        if (outputStream == null) {
            outputStream = new CachedServletOutputStream(cachedBody);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called");
        }
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(cachedBody, getCharacterEncoding()));
        }
        return writer;
    }

    public byte[] getCachedBody() throws IOException {
        if (writer != null) {
            writer.flush();
        }
        if (outputStream != null) {
            outputStream.flush();
        }
        return cachedBody.toByteArray();
    }

    private static class CachedServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream cache;

        public CachedServletOutputStream(ByteArrayOutputStream cache) {
            this.cache = cache;
        }

        @Override
        public void write(int b) throws IOException {
            cache.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            throw new UnsupportedOperationException();
        }
    }
}
