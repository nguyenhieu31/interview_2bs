package org.com.document.repository;

import org.com.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    @Query("select d from Document d " +
            "where " +
            "(:taxCode = '' or d.taxCode = :taxCode) " +
            "and (:companyName = '' or LOWER(d.companyName) like CONCAT('%', :companyName, '%')) " +
            "and (:provinceCode = '' or d.province.code = :provinceCode) " +
            "and (:documentType = '' or d.documentType.code = :documentType) " +
            "and (:status is null or d.status.id = :status)")
    Page<Document> searchDocuments(@Param("taxCode") String taxCode,
                                   @Param("companyName") String companyName,
                                   @Param("provinceCode") String provinceCode,
                                   @Param("documentType") String documentType,
                                   @Param("status") UUID status,
                                   Pageable pageable
    );
}
