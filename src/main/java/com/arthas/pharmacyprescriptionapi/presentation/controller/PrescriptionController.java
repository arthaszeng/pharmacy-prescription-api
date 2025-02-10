package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.PrescriptionApplicationService;
import com.arthas.pharmacyprescriptionapi.presentation.dto.CreatePrescriptionCommand;
import com.arthas.pharmacyprescriptionapi.presentation.dto.PrescriptionRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<PrescriptionRepresentation> fulfillPrescription(@PathVariable Long id) {
        PrescriptionRepresentation response = PrescriptionRepresentation.fromDomain(
                prescriptionApplicationService.fulfillPrescription(id)
        );
        return ResponseEntity.ok(response);
    }
}