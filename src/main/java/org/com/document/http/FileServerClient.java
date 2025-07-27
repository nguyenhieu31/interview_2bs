package org.com.document.http;

import org.com.document.constant.AppConstant;
import org.com.document.dto.request.FileUploadRequest;
import org.com.document.dto.response.FileDataResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface FileServerClient {
    @PostExchange(url = AppConstant.UPLOAD_FILE)
    FileDataResponse uploadFile(@RequestBody FileUploadRequest request);
}
