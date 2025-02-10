package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PharmacyDomainService {
    private final PharmacyRepositoryInterface pharmacyRepository;

    public List<PharmacyDomain> getAllPharmacies() {
        return pharmacyRepository.findAll()
                .stream()
                .map(schema -> PharmacyDomain.fromSchema(schema, true))
                .collect(Collectors.toList());
    }
}