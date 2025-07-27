package org.com.document.repository;

import org.com.document.entity.DocumentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, UUID> {
}
