package org.com.document.repository;

import org.com.document.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    Optional<Ward> findByCodeAndProvince_Code(String wardCode, String provinceCode);

    Optional<Ward> findByCode(String wardCode);
}
