package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.PrescriptionApplicationService;
import com.arthas.pharmacyprescriptionapi.presentation.dto.CreatePrescriptionCommand;
import com.arthas.pharmacyprescriptionapi.presentation.dto.PrescriptionRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionApplicationService prescriptionApplicationService;

    @PostMapping
    public ResponseEntity<PrescriptionRepresentation> createPrescription(@RequestBody CreatePrescriptionCommand command) {
        PrescriptionRepresentation response = PrescriptionRepresentation.fromDomain(
                prescriptionApplicationService.createPrescription(command.toDomain())
        );

        return ResponseEntity.ok(response);
    }
}