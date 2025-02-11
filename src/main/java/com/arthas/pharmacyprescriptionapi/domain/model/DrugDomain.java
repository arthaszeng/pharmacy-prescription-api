package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugDomain {
    private Long id;
    private String name;
    private String manufacturer;
    private String batchNumber;
    private Date expiryDate;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted = false;

    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

    public static DrugDomain fromSchema(DrugSchema schema) {
        return new DrugDomain(
                schema.getId(),
                schema.getName(),
                schema.getManufacturer(),
                schema.getBatchNumber(),
                schema.getExpiryDate(),
                schema.getStock(),
                schema.getCreatedAt(),
                schema.getUpdatedAt(),
                schema.getDeleted()
        );
    }

    public DrugSchema toSchema() {
        return new DrugSchema(
                id,
                name,
                manufacturer,
                batchNumber,
                expiryDate,
                stock,
                createdAt != null ? createdAt : LocalDateTime.now(),
                updatedAt != null ? updatedAt : LocalDateTime.now(),
                deleted
        );
    }
}