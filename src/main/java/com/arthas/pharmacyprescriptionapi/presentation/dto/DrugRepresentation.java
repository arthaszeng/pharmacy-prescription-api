package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugRepresentation {
    private Long id;
    private String name;
    private String manufacturer;
    private String batchNumber;
    private Date expiryDate;
    private int stock;

    public static DrugRepresentation fromDomain(DrugDomain drug, int allocatedStock) {
        return DrugRepresentation.builder()
                .id(drug.getId())
                .name(drug.getName())
                .manufacturer(drug.getManufacturer())
                .batchNumber(drug.getBatchNumber())
                .expiryDate(drug.getExpiryDate())
                .stock(allocatedStock)
                .build();
    }
}