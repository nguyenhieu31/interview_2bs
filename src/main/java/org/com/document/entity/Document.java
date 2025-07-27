package org.com.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 20)
    private String companyPhoneNumber;

    private String companyFax;

    private String address;

    @Column(nullable = false)
    private String companyEmail;

    private String taxCode;

    @Column(length = 25)
    private String nswCode;

    private LocalDateTime receiveTime;

    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "document")
    private List<DocumentHistory> documentHistories;

    @OneToMany(mappedBy = "document")
    private List<DocumentAttachment> documentAttachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code")
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type")
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
}
