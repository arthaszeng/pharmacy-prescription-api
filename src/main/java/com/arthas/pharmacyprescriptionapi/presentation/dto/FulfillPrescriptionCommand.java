package com.arthas.pharmacyprescriptionapi.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FulfillPrescriptionCommand {
    private Long prescriptionId;
}