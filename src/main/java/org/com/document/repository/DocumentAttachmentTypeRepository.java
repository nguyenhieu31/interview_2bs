package org.com.document.repository;

import org.com.document.entity.DocumentAttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentAttachmentTypeRepository extends JpaRepository<DocumentAttachmentType, String> {
    Optional<DocumentAttachmentType> findByCode(String code);
}
