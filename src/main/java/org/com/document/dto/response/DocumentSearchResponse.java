package org.com.document.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSearchResponse {
    private UUID id;
    private String companyName;
    private String companyPhoneNumber;
    private String companyFax;
    private String address;
    private String companyEmail;
    private String taxCode;
    private String nswCode;
    private LocalDateTime receiveTime;
    private LocalDateTime lastModifiedTime;
    private String documentTypeName;
    private String statusName;
    private String provinceName;
    private String wardName;
}
