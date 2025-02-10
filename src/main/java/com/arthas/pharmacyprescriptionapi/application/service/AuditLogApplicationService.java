package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.AuditLogDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.AuditLogDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogApplicationService {
    private final AuditLogDomainService auditLogDomainService;

    @Transactional(readOnly = true)
    public Page<AuditLogDomain> getAuditLogs(Long patientId, Long pharmacyId, String status, Pageable pageable) {
        return auditLogDomainService.getAuditLogs(
                Optional.ofNullable(patientId),
                Optional.ofNullable(pharmacyId),
                Optional.ofNullable(status),
                pageable
        );
    }
}