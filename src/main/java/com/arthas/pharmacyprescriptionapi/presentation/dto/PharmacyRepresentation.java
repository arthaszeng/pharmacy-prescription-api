package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRepresentation {
    private Long id;
    private String name;
    private List<DrugRepresentation> drugs;

    public static PharmacyRepresentation fromDomain(PharmacyDomain domain) {
        return new PharmacyRepresentation(
                domain.getId(),
                domain.getName(),
                domain.getAllocations() != null
                        ? domain.getAllocations().stream()
                        .map(allocation -> DrugRepresentation.fromDomain(allocation.getDrug()))
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}