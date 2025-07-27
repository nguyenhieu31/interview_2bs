package org.com.document.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.document.dto.request.DocumentHistoryRequest;
import org.com.document.dto.response.ApiResponse;
import org.com.document.service.DocumentHistoryService;
import org.com.document.utils.CreateApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document-history")
@RequiredArgsConstructor
public class DocumentHistoryController {
    private final DocumentHistoryService documentHistoryService;
    @PostMapping("/save")
    public ApiResponse<String> saveDocumentHistory(@RequestBody @Valid DocumentHistoryRequest request) throws Exception {
        return CreateApiResponse.createResponse(documentHistoryService.save(request));
    }
}
