package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePrescriptionCommand {
    private Long patientId;
    private Long pharmacyId;
    private List<PrescriptionDrugRequest> drugs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrescriptionDrugRequest {
        private Long drugId;
        private int dosage;
    }

    public PrescriptionDomain toDomain() {
        List<PrescriptionDrugDomain> prescriptionDrugs = this.drugs.stream()
                .map(d -> PrescriptionDrugDomain.builder()
                        .drug(DrugDomain.builder().id(d.getDrugId()).build())
                        .dosage(d.getDosage())
                        .build())
                .toList();

        return PrescriptionDomain.builder()
                .patient(PatientDomain.builder().id(this.patientId).build())
                .pharmacy(PharmacyDomain.builder().id(this.pharmacyId).build())
                .prescriptionDrugs(prescriptionDrugs)
                .status("PENDING")
                .build();
    }
}