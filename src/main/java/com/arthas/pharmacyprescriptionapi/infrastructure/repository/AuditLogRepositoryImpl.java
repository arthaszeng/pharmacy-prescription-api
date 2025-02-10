package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.domain.repository.AuditLogRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepositoryImpl extends JpaRepository<AuditLogSchema, Long>,
        AuditLogRepositoryInterface, JpaSpecificationExecutor<AuditLogSchema> {
}