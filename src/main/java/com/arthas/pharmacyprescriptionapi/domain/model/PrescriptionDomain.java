package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionDrugSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionSchema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDomain {
    private Long id;
    private PatientDomain patient;
    private PharmacyDomain pharmacy;
    private List<PrescriptionDrugDomain> prescriptionDrugs;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PrescriptionDomain fromSchema(PrescriptionSchema schema) {
        return PrescriptionDomain.builder()
                .id(schema.getId())
                .patient(PatientDomain.fromSchema(schema.getPatient()))
                .pharmacy(PharmacyDomain.fromSchema(schema.getPharmacy(), false))
                .prescriptionDrugs(schema.getPrescriptionDrugs().stream()
                        .map(PrescriptionDrugDomain::fromSchema)
                        .collect(Collectors.toList()))
                .status(schema.getStatus())
                .createdAt(schema.getCreatedAt())
                .updatedAt(schema.getUpdatedAt())
                .build();
    }

    public PrescriptionSchema toSchema() {
        PrescriptionSchema schema = PrescriptionSchema.builder()
                .id(this.id)
                .patient(this.patient.toSchema())
                .pharmacy(this.pharmacy.toSchema())
                .prescriptionDrugs(new ArrayList<>())
                .status(this.status)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .updatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .build();

        for (PrescriptionDrugDomain drugDomain : prescriptionDrugs) {
            PrescriptionDrugSchema drugSchema = drugDomain.toSchema();
            drugSchema.setPrescription(schema);
            schema.getPrescriptionDrugs().add(drugSchema);
        }

        return schema;
    }
}