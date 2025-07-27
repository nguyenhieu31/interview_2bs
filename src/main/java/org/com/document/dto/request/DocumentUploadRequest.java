package org.com.document.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentUploadRequest {
    @NotBlank(message = "The company name isn't blank.")
    private String companyName;

    @NotBlank(message = "The phone number isn't blank.")
    @Pattern(regexp = "^\\d{10}$", message = "The phone number must be have 10 digits.")
    private String companyPhoneNumber;

    @NotBlank(message = "The company fax isn't blank.")
    private String companyFax;

    @NotBlank(message = "address isn't blank.")
    private String address;

    @Email(message = "Email invalid.")
    @NotBlank(message = "Email isn't blank.")
    private String companyEmail;

    @NotBlank(message = "The tax code isn't blank.")
    @Pattern(regexp = "^\\d{10}(\\d{3})?$", message = "The tax code must be have 10 or 13 digits.")
    private String taxCode;

    @NotBlank(message = "Code NSW isn't blank.")
    private String nswCode;

    private String provinceCode;

    private String wardCode;

    @NotNull(message = "The status is required.")
    private UUID status;

    @NotBlank(message = "The document type isn't blank.")
    private String documentType;
}
