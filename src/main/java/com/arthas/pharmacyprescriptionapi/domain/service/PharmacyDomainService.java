package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PharmacyDomainService {
    private final PharmacyRepositoryInterface pharmacyRepository;

    public Page<PharmacyDomain> getAllPharmacies(Pageable pageable) {
        return pharmacyRepository.findAll(pageable)
                .map(schema -> PharmacyDomain.fromSchema(schema, true)); // ✅ Include allocations
    }
}