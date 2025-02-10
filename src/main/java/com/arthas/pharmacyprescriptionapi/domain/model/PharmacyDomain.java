package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDomain {
    private Long id;
    private String name;
    private List<PharmacyDrugAllocationDomain> allocations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PharmacyDomain fromSchema(PharmacySchema schema, boolean includeAllocations) {
        return PharmacyDomain.builder()
                .id(schema.getId())
                .name(schema.getName())
                .allocations(includeAllocations
                        ? Optional.ofNullable(schema.getAllocations())
                        .map(allocations -> allocations.stream()
                                .map(allocation -> PharmacyDrugAllocationDomain.fromSchema(allocation, true))
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
                        : null
                )
                .createdAt(Optional.ofNullable(schema.getCreatedAt()).orElse(LocalDateTime.now()))
                .updatedAt(Optional.ofNullable(schema.getUpdatedAt()).orElse(LocalDateTime.now()))
                .build();
    }

    public PharmacySchema toSchema() {
        return PharmacySchema.builder()
                .id(this.id)
                .name(this.name)
                .createdAt(Optional.ofNullable(this.createdAt).orElse(LocalDateTime.now()))
                .updatedAt(Optional.ofNullable(this.updatedAt).orElse(LocalDateTime.now()))
                .allocations(Optional.ofNullable(this.allocations)
                        .map(allocations -> allocations.stream()
                                .map(PharmacyDrugAllocationDomain::toSchema)
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList()))
                .build();
    }
}