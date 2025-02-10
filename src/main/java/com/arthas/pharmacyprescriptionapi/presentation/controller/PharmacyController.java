package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.PharmacyApplicationService;
import com.arthas.pharmacyprescriptionapi.presentation.dto.PharmacyRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {
    private final PharmacyApplicationService pharmacyApplicationService;

    @GetMapping
    public ResponseEntity<Page<PharmacyRepresentation>> getAllPharmacies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PharmacyRepresentation> pharmacies = pharmacyApplicationService.getAllPharmacies(page, size)
                .map(PharmacyRepresentation::fromDomain);
        return ResponseEntity.ok(pharmacies);
    }
}