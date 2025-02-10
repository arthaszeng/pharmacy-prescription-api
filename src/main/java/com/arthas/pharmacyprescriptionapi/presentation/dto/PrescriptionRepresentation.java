package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRepresentation {
    private Long id;
    private String status;
    private List<PrescriptionDrugRepresentation> drugs;

    public static PrescriptionRepresentation fromDomain(PrescriptionDomain domain) {
        return PrescriptionRepresentation.builder()
                .id(domain.getId())
                .status(domain.getStatus())
                .drugs(domain.getPrescriptionDrugs().stream()
                        .map(PrescriptionDrugRepresentation::fromDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PrescriptionDrugRepresentation {
        private Long drugId;
        private String name;
        private String batchNumber;
        private int dosage;

        public static PrescriptionDrugRepresentation fromDomain(PrescriptionDrugDomain domain) {
            return PrescriptionDrugRepresentation.builder()
                    .drugId(domain.getDrug().getId())
                    .name(domain.getDrug().getName())
                    .batchNumber(domain.getDrug().getBatchNumber())
                    .dosage(domain.getDosage())
                    .build();
        }
    }
}