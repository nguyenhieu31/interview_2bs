package org.com.document.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequest {
    private String documentType;
    private String nswCode;
    private String base64Data;
    private String fileName;
    private Boolean isExportToWord;
}
