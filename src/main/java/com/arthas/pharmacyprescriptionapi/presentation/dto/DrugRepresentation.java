package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugRepresentation {
    private Long id;
    private String name;
    private String manufacturer;
    private String batchNumber;
    private Date expiryDate;
    private int stock;

    public static DrugRepresentation fromDomain(DrugDomain domain) {
        return new DrugRepresentation(
                domain.getId(),
                domain.getName(),
                domain.getManufacturer(),
                domain.getBatchNumber(),
                domain.getExpiryDate(),
                domain.getStock()
        );
    }
}