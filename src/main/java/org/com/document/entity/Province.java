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
@Table(name = "province")
public class Province {
    @Id
    @Column(length = 25)
    private String code;

    private String name;

    @OneToMany(mappedBy = "province")
    private List<Document> documents;

    @OneToMany(mappedBy = "province")
    private List<Ward> wards;
}
