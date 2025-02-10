package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.AuditLogApplicationService;
import com.arthas.pharmacyprescriptionapi.domain.model.AuditLogDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogApplicationService auditLogApplicationService;

    @GetMapping
    public ResponseEntity<Page<AuditLogDomain>> getAuditLogs(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long pharmacyId,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        return ResponseEntity.ok(auditLogApplicationService.getAuditLogs(patientId, pharmacyId, status, pageable));
    }
}