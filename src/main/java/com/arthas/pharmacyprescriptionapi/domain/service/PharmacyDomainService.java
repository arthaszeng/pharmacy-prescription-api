package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDrugAllocationDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyDrugAllocationRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyDomainService {
    private final PharmacyRepositoryInterface pharmacyRepository;
    private final PharmacyDrugAllocationRepositoryInterface allocationRepository;

    @Transactional(readOnly = true)
    public Page<PharmacyDomain> getAllPharmacies(Pageable pageable) {
        return pharmacyRepository.findAll(pageable)
                .map(schema -> PharmacyDomain.fromSchema(schema, true));
    }

    @Transactional(readOnly = true)
    public PharmacyDomain getPharmacyById(Long id) {
        return pharmacyRepository.findById(id)
                .map(schema -> PharmacyDomain.fromSchema(schema, true))
                .orElseThrow(() -> new NoSuchElementException("Pharmacy ID " + id + " does not exist."));
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDrugDomain> validateAndAllocateDrugs(PharmacyDomain pharmacy, List<PrescriptionDrugDomain> requestedDrugs) {
        Map<Long, PharmacyDrugAllocationDomain> allocationMap = getAllocationMap(pharmacy);
        return requestedDrugs.stream()
                .map(drug -> allocateDrug(drug, allocationMap))
                .toList();
    }

    @Transactional
    public void validateAndDeductStock(PharmacyDomain pharmacy, List<PrescriptionDrugDomain> prescriptionDrugs) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                Map<Long, PharmacyDrugAllocationDomain> allocationMap = getAllocationMap(pharmacy);

                List<PharmacyDrugAllocationSchema> updatedAllocations = prescriptionDrugs.stream()
                        .map(drug -> deductStock(allocationMap, drug))
                        .map(PharmacyDrugAllocationDomain::toSchema)
                        .toList();

                allocationRepository.saveAll(updatedAllocations);

                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                log.warn("Optimistic lock conflict on stock update (attempt {}/{})", attempt, maxRetries);

                if (attempt == maxRetries) {
                    throw new IllegalStateException("Stock update failed after multiple retries. Please try again.");
                }
            }
        }
    }

    private Map<Long, PharmacyDrugAllocationDomain> getAllocationMap(PharmacyDomain pharmacy) {
        return pharmacy.getAllocations().stream()
                .filter(PharmacyDrugAllocationDomain::isActive)
                .collect(Collectors.toMap(PharmacyDrugAllocationDomain::getDrugId, allocation -> allocation));
    }

    private PrescriptionDrugDomain allocateDrug(PrescriptionDrugDomain prescriptionDrug, Map<Long, PharmacyDrugAllocationDomain> allocationMap) {
        PharmacyDrugAllocationDomain allocation = getValidAllocation(prescriptionDrug, allocationMap);
        return PrescriptionDrugDomain.builder()
                .drug(allocation.getDrug())
                .dosage(prescriptionDrug.getDosage())
                .build();
    }

    private PharmacyDrugAllocationDomain deductStock(Map<Long, PharmacyDrugAllocationDomain> allocationMap, PrescriptionDrugDomain prescriptionDrug) {
        PharmacyDrugAllocationDomain allocation = getValidAllocation(prescriptionDrug, allocationMap);

        allocation.setAllocatedStock(allocation.getAllocatedStock() - prescriptionDrug.getDosage());
        allocation.setUpdatedAt(LocalDateTime.now());

        log.info("Deducted {} units of Drug ID {} from Pharmacy ID {}. Remaining stock: {}",
                prescriptionDrug.getDosage(), prescriptionDrug.getDrugId(),
                allocation.getPharmacyId(), allocation.getAllocatedStock());

        return allocation;
    }

    private static PharmacyDrugAllocationDomain getValidAllocation(PrescriptionDrugDomain prescriptionDrug,
                                                                   Map<Long, PharmacyDrugAllocationDomain> allocationMap) {
        return Optional.ofNullable(allocationMap.get(prescriptionDrug.getDrugId()))
                .filter(allocation -> allocation.getAllocatedStock() >= prescriptionDrug.getDosage())
                .orElseThrow(() -> new IllegalArgumentException("Insufficient or missing stock for Drug ID " + prescriptionDrug.getDrugId()));
    }
}