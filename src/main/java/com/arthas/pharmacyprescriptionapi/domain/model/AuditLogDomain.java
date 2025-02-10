package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDomain {
    private Long id;
    private Long prescriptionId;
    private Long patientId;
    private Long pharmacyId;
    private String status;
    private String failureReason;
    private List<Map<String, Object>> requestedDrugs;
    private List<Map<String, Object>> dispensedDrugs;
    private LocalDateTime createdAt;

    public static AuditLogDomain fromSchema(AuditLogSchema schema) {
        return AuditLogDomain.builder()
                .id(schema.getId())
                .prescriptionId(schema.getPrescriptionId())
                .patientId(schema.getPatientId())
                .pharmacyId(schema.getPharmacyId())
                .status(schema.getStatus())
                .failureReason(schema.getFailureReason())
                .requestedDrugs(schema.getRequestedDrugs())
                .dispensedDrugs(schema.getDispensedDrugs())
                .createdAt(schema.getCreatedAt())
                .build();
    }

    public AuditLogSchema toSchema() {
        return AuditLogSchema.builder()
                .id(this.id)
                .prescriptionId(this.prescriptionId)
                .patientId(this.patientId)
                .pharmacyId(this.pharmacyId)
                .status(this.status)
                .failureReason(this.failureReason)
                .requestedDrugs(this.requestedDrugs)
                .dispensedDrugs(this.dispensedDrugs)
                .createdAt(this.createdAt)
                .build();
    }
}