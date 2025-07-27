package org.com.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentHistoryRequest {
    @NotBlank(message = "Title isn't blank")
    private String title;

    @NotBlank(message = "Content isn't blank")
    private String content;

    @NotBlank(message = "Reason isn't blank")
    private String reason;

    @NotBlank(message = "Action isn't blank")
    private String action;

    @NotBlank(message = "Message content isn't blank")
    private String messageContent;

    @NotNull(message = "Document ID is required")
    private UUID documentId;
}
