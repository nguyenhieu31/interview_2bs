package org.com.document.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentAttachmentRequest {
    @NotNull(message = "Document ID is required.")
    private UUID documentId;

    @NotBlank(message = "Doc attachment type code isn't blank.")
    private String docAttachmentTypeCode;

    @NotNull(message = "File is required.")
    private MultipartFile file;

}
