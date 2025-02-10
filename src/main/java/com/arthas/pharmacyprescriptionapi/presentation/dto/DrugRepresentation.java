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

    public static DrugRepresentation fromDomain(DrugDomain domain) {
        return DrugRepresentation.builder()
                .id(domain.getId())
                .name(domain.getName())
                .manufacturer(domain.getManufacturer())
                .batchNumber(domain.getBatchNumber())
                .expiryDate(domain.getExpiryDate())
                .stock(domain.getStock())
                .build();
    }
}