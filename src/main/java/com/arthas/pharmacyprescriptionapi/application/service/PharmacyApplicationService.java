package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyApplicationService {
    private final PharmacyDomainService pharmacyDomainService;

    @Transactional(readOnly = true)
    public List<PharmacyDomain> getAllPharmacies() {
        return pharmacyDomainService.getAllPharmacies();
    }
}