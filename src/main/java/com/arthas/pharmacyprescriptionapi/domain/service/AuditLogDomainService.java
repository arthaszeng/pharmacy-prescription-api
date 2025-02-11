package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.AuditLogDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.AuditLogRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogDomainService {
    private final AuditLogRepositoryInterface auditLogRepository;

    public void logFulfillSuccess(PrescriptionDomain prescription) {
        auditLogRepository.save(buildAuditLog(prescription, "SUCCESS", "N/A").toSchema());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFulfillFailure(PrescriptionDomain prescription, String failureReason) {
        auditLogRepository.save(buildAuditLog(prescription, "FAILURE", failureReason).toSchema());
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDomain> getAuditLogs(Optional<Long> patientId, Optional<Long> pharmacyId, Optional<String> status, Pageable pageable) {
        Specification<AuditLogSchema> spec = Specification.where(null);

        patientId.ifPresent(id -> spec.and((root, query, cb) -> cb.equal(root.get("patientId"), id)));
        pharmacyId.ifPresent(id -> spec.and((root, query, cb) -> cb.equal(root.get("pharmacyId"), id)));
        status.ifPresent(s -> spec.and((root, query, cb) -> cb.equal(root.get("status"), s)));

        return auditLogRepository.findAll(spec, pageable)
                .map(AuditLogDomain::fromSchema);
    }


    private static AuditLogDomain buildAuditLog(PrescriptionDomain prescription, String status, String failureReason) {
        List<Map<String, Object>> drugs = prescription.getPrescriptionDrugs().stream()
                .map(drug -> Map.<String, Object>of(
                        "drugId", drug.getDrug().getId(),
                        "name", drug.getDrug().getName(),
                        "quantity", drug.getDosage()
                ))
                .collect(Collectors.toList());

        return AuditLogDomain.builder()
                .prescriptionId(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .pharmacyId(prescription.getPharmacy().getId())
                .status(status)
                .failureReason(failureReason)
                .requestedDrugs(drugs)
                .dispensedDrugs(status.equals("SUCCESS") ? drugs : List.of())
                .createdAt(prescription.getCreatedAt())
                .build();
    }
}