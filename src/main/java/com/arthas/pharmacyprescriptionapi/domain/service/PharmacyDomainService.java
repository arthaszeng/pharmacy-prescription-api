package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDrugAllocationDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyDomainService {
    private final PharmacyRepositoryInterface pharmacyRepository;

    public PharmacyDomain getPharmacyById(Long id) {
        return pharmacyRepository.findById(id)
                .map(schema -> PharmacyDomain.fromSchema(schema, true))
                .orElseThrow(() -> {
                    log.warn("Pharmacy ID {} not found", id);
                    return new NoSuchElementException("Pharmacy ID " + id + " does not exist.");
                });
    }

    public List<PrescriptionDrugDomain> validateAndAllocateDrugs(PharmacyDomain pharmacy,
                                                                 List<PrescriptionDrugDomain> requestedDrugs) {
        // Convert pharmacy allocations to a lookup map (Drug ID -> Allocation)
        Map<Long, PharmacyDrugAllocationDomain> allocationMap = pharmacy.getAllocations().stream()
                .collect(Collectors.toMap(PharmacyDrugAllocationDomain::getDrugId, Function.identity()));

        return requestedDrugs.stream()
                .map(prescriptionDrug -> {
                    Long drugId = prescriptionDrug.getDrugId();

                    // Fetch allocation from preloaded map
                    PharmacyDrugAllocationDomain allocation = Optional.ofNullable(allocationMap.get(drugId))
                            .orElseThrow(() -> {
                                log.warn("Pharmacy ID {} does not have drug ID {} allocated", pharmacy.getId(), drugId);
                                return new NoSuchElementException("Pharmacy does not have drug ID " + drugId + " allocated.");
                            });

                    // Validate stock availability
                    Optional.of(allocation)
                            .filter(allocationDomain -> allocationDomain.getAllocatedStock() >= prescriptionDrug.getDosage())
                            .orElseThrow(() -> {
                                log.warn("Insufficient stock for drug ID {} in pharmacy ID {}", drugId, pharmacy.getId());
                                return new IllegalArgumentException("Insufficient stock for drug ID " + drugId);
                            });

                    return PrescriptionDrugDomain.builder()
                            .drug(allocation.getDrug())
                            .dosage(prescriptionDrug.getDosage())
                            .build();
                })
                .toList();
    }

    public Page<PharmacyDomain> getAllPharmacies(Pageable pageable) {
        return pharmacyRepository.findAll(pageable)
                .map(schema -> PharmacyDomain.fromSchema(schema, true));
    }
}