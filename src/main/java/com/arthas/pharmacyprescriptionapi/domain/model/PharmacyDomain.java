package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDomain {
    private Long id;
    private String name;
    private List<PharmacyDrugAllocationDomain> allocations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PharmacyDomain fromSchema(PharmacySchema schema, boolean includeAllocations) {
        return new PharmacyDomain(
                schema.getId(),
                schema.getName(),
                includeAllocations ? schema.getAllocations().stream()
                        .map(allocation -> PharmacyDrugAllocationDomain.fromSchema(allocation, false))
                        .collect(Collectors.toList()) : null,
                schema.getCreatedAt(),
                schema.getUpdatedAt()
        );
    }
}