package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PharmacyApplicationService {
    private final PharmacyDomainService pharmacyDomainService;

    @Transactional(readOnly = true)
    public Page<PharmacyDomain> getAllPharmacies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pharmacyDomainService.getAllPharmacies(pageable);
    }
}