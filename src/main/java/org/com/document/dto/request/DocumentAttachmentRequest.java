package org.com.document.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentAttachmentRequest {
    @NotBlank(message = "Document ID isn't blank")
    private String documentId;

    @NotBlank(message = "Doc attachment type code isn't blank.")
    private String docAttachmentTypeCode;

    @NotNull(message = "File is required.")
    private MultipartFile file;

}
