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
        return new DrugDomain(null, name, manufacturer, batchNumber, expiryDate, stock, LocalDateTime.now(), LocalDateTime.now(), false);
    }
}