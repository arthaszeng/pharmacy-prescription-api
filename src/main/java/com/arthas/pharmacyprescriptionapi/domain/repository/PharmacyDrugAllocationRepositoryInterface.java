package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;

import java.util.List;

public interface PharmacyDrugAllocationRepositoryInterface {
    List<PharmacyDrugAllocationSchema> saveAll(Iterable<PharmacyDrugAllocationSchema> updatedAllocations);
}