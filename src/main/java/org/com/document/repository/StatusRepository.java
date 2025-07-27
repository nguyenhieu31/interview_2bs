package org.com.document.repository;

import org.com.document.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface StatusRepository extends JpaRepository<Status, UUID> {
}
