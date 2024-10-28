package dev.inspector.spring.utils.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedBodyServletInputStream.class);

    private final InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cacheBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cacheBody);
    }

    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            LOGGER.error("Exception occured during checking if input stream is finished", e);
            return false;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        // TODO: Not yet implemented
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
