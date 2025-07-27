package org.com.document.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentSearchRequest {
    @NotNull(message = "Tax code is required.")
    private String taxCode;

    @NotNull(message = "The company name is required.")
    private String companyName;

    @NotNull(message = "The document type is required.")
    private String documentType;

    @NotNull(message = "Province code is required.")
    private String provinceCode;

    @NotNull(message = "Status is required.")
    private String status;
}
