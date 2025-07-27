package org.com.document.service;

import org.com.document.dto.request.DocumentHistoryRequest;

public interface DocumentHistoryService {
    String save(DocumentHistoryRequest request) throws Exception;
}
