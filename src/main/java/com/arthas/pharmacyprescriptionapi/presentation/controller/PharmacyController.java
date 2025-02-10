package com.arthas.pharmacyprescriptionapi.presentation.controller;

import com.arthas.pharmacyprescriptionapi.application.service.PharmacyApplicationService;
import com.arthas.pharmacyprescriptionapi.presentation.dto.PharmacyRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {
    private final PharmacyApplicationService pharmacyApplicationService;

    @GetMapping
    public ResponseEntity<List<PharmacyRepresentation>> getAllPharmacies() {
        List<PharmacyRepresentation> pharmacies = pharmacyApplicationService.getAllPharmacies()
                .stream()
                .map(PharmacyRepresentation::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pharmacies);
    }
}