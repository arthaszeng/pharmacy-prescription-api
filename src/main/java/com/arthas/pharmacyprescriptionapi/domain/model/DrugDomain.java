package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugDomain {
    private Long id;
    private String name;
    private String manufacturer;
    private String batchNumber;
    private Date expiryDate;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

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