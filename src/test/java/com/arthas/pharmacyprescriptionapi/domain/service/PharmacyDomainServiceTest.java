package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDrugAllocationDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyDrugAllocationRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PharmacyDomainServiceTest {

    @Mock
    private PharmacyRepositoryInterface pharmacyRepository;

    @Mock
    private PharmacyDrugAllocationRepositoryInterface allocationRepository;

    @InjectMocks
    private PharmacyDomainService pharmacyDomainService;

    private PharmacyDrugAllocationDomain mockAllocation;
    private PharmacyDomain mockPharmacy;
    private PrescriptionDrugDomain mockPrescriptionDrug;
    private DrugDomain mockDrug;
    private PharmacySchema mockPharmacySchema;

    @BeforeEach
    void setUp() {
        mockDrug = DrugDomain.builder()
                .id(10L)
                .name("Paracetamol")
                .stock(100)
                .manufacturer("Test Manufacturer")
                .expiryDate(Date.valueOf("2025-12-31"))
                .build();

        mockPharmacy = PharmacyDomain.builder()
                .id(1L)
                .allocations(List.of()) // Initially empty, will be set later
                .build();

        mockAllocation = PharmacyDrugAllocationDomain.builder()
                .drug(mockDrug)
                .allocatedStock(50)
                .pharmacy(mockPharmacy)
                .build();

        mockPharmacy = PharmacyDomain.builder()
                .id(1L)
                .allocations(List.of(mockAllocation))
                .build();

        mockPharmacySchema = PharmacySchema.builder()
                .id(1L)
                .allocations(List.of(mockAllocation.toSchema())) // Ensure it's correctly populated
                .build();

        mockPrescriptionDrug = PrescriptionDrugDomain.builder()
                .drug(mockDrug)
                .dosage(10)
                .build();
    }

    /**
     * Test: `getPharmacyById()` should return correct pharmacy.
     */
    @Test
    void getPharmacyById_shouldReturnPharmacyIfExists() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(mockPharmacySchema));

        PharmacyDomain result = pharmacyDomainService.getPharmacyById(1L);

        assertThat(result.getId()).isEqualTo(mockPharmacy.getId());
    }

    /**
     * Test: `getAllPharmacies()` should return paginated results.
     */
    @Test
    void getAllPharmacies_shouldReturnPaginatedResults() {
        Page<PharmacySchema> mockPage = new PageImpl<>(List.of(mockPharmacySchema));

        when(pharmacyRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<PharmacyDomain> result = pharmacyDomainService.getAllPharmacies(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
    }

    /**
     * Test: `validateAndDeductStock()` should fail after maximum retries.
     */
    @Test
    void validateAndDeductStock_shouldFailAfterMaxRetriesExceeded() {
        when(allocationRepository.saveAll(any()))
                .thenThrow(new ObjectOptimisticLockingFailureException(PharmacyDrugAllocationSchema.class, 1L))
                .thenThrow(new ObjectOptimisticLockingFailureException(PharmacyDrugAllocationSchema.class, 1L))
                .thenThrow(new ObjectOptimisticLockingFailureException(PharmacyDrugAllocationSchema.class, 1L));

        assertThatThrownBy(() -> pharmacyDomainService.validateAndDeductStock(mockPharmacy, List.of(mockPrescriptionDrug)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stock update failed after multiple retries");

        verify(allocationRepository, times(3)).saveAll(any());
    }

    /**
     * Test: `validateAndDeductStock()` should retry and succeed.
     */
    @Test
    void validateAndDeductStock_shouldRetryOnOptimisticLockingFailureAndSucceed() {
        when(allocationRepository.saveAll(any()))
                .thenThrow(new ObjectOptimisticLockingFailureException(PharmacyDrugAllocationSchema.class, 1L))
                .thenThrow(new ObjectOptimisticLockingFailureException(PharmacyDrugAllocationSchema.class, 1L))
                .thenReturn(List.of(mockAllocation.toSchema()));

        assertThatCode(() -> pharmacyDomainService.validateAndDeductStock(mockPharmacy, List.of(mockPrescriptionDrug)))
                .doesNotThrowAnyException();

        verify(allocationRepository, times(3)).saveAll(any());
    }

    /**
     * Test: `validateAndAllocateDrugs()` should correctly allocate stock.
     */
    @Test
    void validateAndAllocateDrugs_shouldAllocateCorrectly() {
        List<PrescriptionDrugDomain> allocatedDrugs = pharmacyDomainService.validateAndAllocateDrugs(mockPharmacy, List.of(mockPrescriptionDrug));

        assertThat(allocatedDrugs).hasSize(1);
        assertThat(allocatedDrugs.get(0).getDrugId()).isEqualTo(mockPrescriptionDrug.getDrugId());
    }

    /**
     * Test: `getPharmacyById()` should throw `NoSuchElementException` if pharmacy does not exist.
     */
    @Test
    void getPharmacyById_shouldThrowExceptionIfNotExists() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pharmacyDomainService.getPharmacyById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Pharmacy ID 1 does not exist.");
    }
}