package org.com.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_type")
public class DocumentType {
    @Id
    @Column(length = 25)
    private String code;

    private String name;

    @OneToMany(mappedBy = "documentType")
    private List<Document> documents;
}
