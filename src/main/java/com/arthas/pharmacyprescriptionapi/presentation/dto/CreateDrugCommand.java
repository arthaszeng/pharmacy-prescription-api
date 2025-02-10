package com.arthas.pharmacyprescriptionapi.presentation.dto;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
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
public class CreateDrugCommand {
    private String name;
    private String manufacturer;
    private String batchNumber;
    private Date expiryDate;
    private int stock;

    public DrugDomain toDomain() {
        return DrugDomain.builder()
                .name(name)
                .manufacturer(manufacturer)
                .batchNumber(batchNumber)
                .expiryDate(expiryDate)
                .stock(stock)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }
}