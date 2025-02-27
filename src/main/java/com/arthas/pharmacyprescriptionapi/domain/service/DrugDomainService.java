package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.DrugRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<DrugDomain> getAllDrugs(Pageable pageable) {
        return drugRepository.findAll(pageable).map(DrugDomain::fromSchema);
    }

    public DrugDomain getDrugById(Long id) {
        DrugSchema schema = drugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drug ID " + id + " does not exist"));
        return DrugDomain.fromSchema(schema);
    }
}