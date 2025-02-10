package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyDrugAllocationRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyDrugAllocationRepositoryImpl
        extends JpaRepository<PharmacyDrugAllocationSchema, Long>, PharmacyDrugAllocationRepositoryInterface {

    Optional<PharmacyDrugAllocationSchema> findByPharmacyIdAndDrugId(Long pharmacyId, Long drugId);
}