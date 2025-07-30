package org.com.document.service;

import org.com.document.dto.request.DocumentSearchRequest;
import org.com.document.dto.request.DocumentUploadRequest;
import org.com.document.dto.response.DocumentSearchResponse;
import org.com.document.dto.response.PageResponse;
import org.com.document.entity.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    String save(DocumentUploadRequest request) throws Exception;
    Document checkExist(String documentId);
    PageResponse<List<DocumentSearchResponse>> search(int pageNum, int pageSize, DocumentSearchRequest request) throws Exception;
}
