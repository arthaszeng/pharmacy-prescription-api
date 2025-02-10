package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface AuditLogRepositoryInterface {
    AuditLogSchema save(AuditLogSchema log);

    Page<AuditLogSchema> findAll(Specification<AuditLogSchema> spec, Pageable pageable);
}