package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.DrugApplicationService;
import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
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
        DrugDomain drug = drugApplicationService.addDrug(command.toDomain());
        DrugRepresentation response = DrugRepresentation.fromDomain(drug, drug.getStock());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DrugRepresentation>> getAllDrugs(Pageable pageable) {
        Page<DrugRepresentation> drugs = drugApplicationService.getAllDrugs(pageable)
                .map(drug -> DrugRepresentation.fromDomain(drug, drug.getStock()));
        return ResponseEntity.ok(drugs);
    }
}