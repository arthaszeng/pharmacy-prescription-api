package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyRepresentation {
    private Long id;
    private String name;
    private List<DrugRepresentation> drugs;

    public static PharmacyRepresentation fromDomain(PharmacyDomain domain) {
        return PharmacyRepresentation.builder()
                .id(domain.getId())
                .name(domain.getName())
                .drugs(domain.getAllocations() != null
                        ? domain.getAllocations().stream()
                        .map(allocation -> DrugRepresentation.fromDomain(allocation.getDrug()))
                        .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}