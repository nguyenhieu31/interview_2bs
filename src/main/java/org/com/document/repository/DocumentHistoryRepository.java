package org.com.document.repository;

import org.com.document.entity.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
}
