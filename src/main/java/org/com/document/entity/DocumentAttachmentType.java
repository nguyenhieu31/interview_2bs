package org.com.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_attachment_type")
public class DocumentAttachmentType {
    @Id
    @Column(length = 25)
    private String code;

    @Column(length = 800)
    private String name;

    @OneToMany(mappedBy = "documentAttachmentType")
    private List<DocumentAttachment> documentAttachments;
}
