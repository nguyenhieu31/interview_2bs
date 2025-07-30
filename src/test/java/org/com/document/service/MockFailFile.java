package org.com.document.service;

import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class MockFailFile extends MockMultipartFile {
    private final IOException exception;

    public MockFailFile(String name, String originalFilename, String contentType, byte[] content, IOException exception) {
        super(name, originalFilename, contentType, content);
        this.exception = exception;
    }

    @Override
    public byte[] getBytes() throws IOException {
        throw exception;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw exception;
    }
}
