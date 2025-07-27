package org.com.document.service;

import org.com.document.dto.request.DocumentAttachmentRequest;

public interface DocumentAttachmentService {
    String save(DocumentAttachmentRequest request) throws Exception;
}
