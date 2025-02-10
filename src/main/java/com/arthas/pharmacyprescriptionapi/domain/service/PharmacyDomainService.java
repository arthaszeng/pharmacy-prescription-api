package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyDrugAllocationRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyDomainService {
    private final PharmacyRepositoryInterface pharmacyRepository;
    private final PharmacyDrugAllocationRepositoryInterface allocationRepository;
    private final DrugDomainService drugDomainService;

    public PharmacyDomain getPharmacyById(Long id) {
        PharmacySchema schema = pharmacyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacy ID " + id + " does not exist"));
        return PharmacyDomain.fromSchema(schema, false);
    }

    public List<PrescriptionDrugDomain> validateAndAllocateDrugs(Long pharmacyId, List<PrescriptionDrugDomain> requestedDrugs) {
        return requestedDrugs.stream().map(prescriptionDrug -> {
            DrugDomain drug = drugDomainService.getDrugById(prescriptionDrug.getDrug().getId());

            PharmacyDrugAllocationSchema allocation = allocationRepository.findByPharmacyIdAndDrugId(pharmacyId, drug.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Pharmacy does not have drug ID " + drug.getId() + " allocated"));

            if (allocation.getAllocatedStock() < prescriptionDrug.getDosage()) {
                throw new IllegalArgumentException("Insufficient stock for drug ID " + drug.getId());
            }

            return new PrescriptionDrugDomain(null, drug, prescriptionDrug.getDosage());
        }).toList();
    }

    public Page<PharmacyDomain> getAllPharmacies(Pageable pageable) {
        return pharmacyRepository.findAll(pageable)
                .map(schema -> PharmacyDomain.fromSchema(schema, true)); // ✅ Include allocations
    }
}