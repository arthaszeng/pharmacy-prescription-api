package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.DrugApplicationService;
import com.arthas.pharmacyprescriptionapi.presentation.dto.CreateDrugCommand;
import com.arthas.pharmacyprescriptionapi.presentation.dto.DrugRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {

    private final DrugApplicationService drugApplicationService;

    @PostMapping
    public ResponseEntity<DrugRepresentation> addDrug(@RequestBody CreateDrugCommand command) {
        DrugRepresentation response = DrugRepresentation.fromDomain(
                drugApplicationService.addDrug(command.toDomain())
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DrugRepresentation>> getAllDrugs(Pageable pageable) {
        Page<DrugRepresentation> drugs = drugApplicationService.getAllDrugs(pageable)
                .map(DrugRepresentation::fromDomain);
        return ResponseEntity.ok(drugs);
    }
}