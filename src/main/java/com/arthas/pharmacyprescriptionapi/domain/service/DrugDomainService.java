package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.DrugRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugDomainService {
    private final DrugRepositoryInterface drugRepository;

    public DrugDomain addDrug(DrugDomain drug) {
        if (drugRepository.findByBatchNumber(drug.getBatchNumber()).isPresent()) {
            throw new IllegalArgumentException("Batch number already exists");
        }

        DrugSchema savedDrug = drugRepository.save(drug.toSchema());
        return DrugDomain.fromSchema(savedDrug);
    }
}