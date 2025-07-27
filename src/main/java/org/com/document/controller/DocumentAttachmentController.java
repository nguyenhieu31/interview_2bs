package org.com.document.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.document.dto.request.DocumentAttachmentRequest;
import org.com.document.dto.response.ApiResponse;
import org.com.document.service.DocumentAttachmentService;
import org.com.document.utils.CreateApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document-attachment")
@RequiredArgsConstructor
public class DocumentAttachmentController {
    private final DocumentAttachmentService documentAttachmentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadDocument(@ModelAttribute @Valid DocumentAttachmentRequest request) throws Exception {
        return CreateApiResponse.createResponse(documentAttachmentService.save(request));
    }
}
