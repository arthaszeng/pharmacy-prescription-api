package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;

import java.util.Optional;

public interface PharmacyDrugAllocationRepositoryInterface {
    Optional<PharmacyDrugAllocationSchema> findById(Long id);

    Optional<PharmacyDrugAllocationSchema> findByPharmacyIdAndDrugId(Long pharmacyId, Long drugId);
}