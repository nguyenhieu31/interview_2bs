package org.com.document.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.document.dto.request.DocumentSearchRequest;
import org.com.document.dto.request.DocumentUploadRequest;
import org.com.document.dto.response.ApiResponse;
import org.com.document.dto.response.DocumentSearchResponse;
import org.com.document.dto.response.PageResponse;
import org.com.document.service.DocumentService;
import org.com.document.utils.CreateApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    @PostMapping("/save")
    public ApiResponse<String> saveDocument(@RequestBody @Valid DocumentUploadRequest request) throws Exception {
        return CreateApiResponse.createResponse(documentService.save(request));
    }

    @PostMapping("/search")
    public ApiResponse<PageResponse<List<DocumentSearchResponse>>> searchDocument(
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestBody @Valid DocumentSearchRequest request
            ) throws Exception {
        return CreateApiResponse.createResponse(documentService.search(pageNum,pageSize,request));
    }
}
