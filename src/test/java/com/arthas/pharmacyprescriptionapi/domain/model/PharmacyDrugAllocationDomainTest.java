package com.arthas.pharmacyprescriptionapi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class PharmacyDrugAllocationDomainTest {

    private DrugDomain activeDrug;
    private DrugDomain expiredDrug;
    private PharmacyDomain pharmacy;

    @BeforeEach
    void setUp() {
        pharmacy = PharmacyDomain.builder().id(1L).name("Central Pharmacy").build();

        activeDrug = DrugDomain.builder()
                .id(100L)
                .name("Paracetamol")
                .expiryDate(Date.from(LocalDate.now().plusDays(10) // Future expiry
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        expiredDrug = DrugDomain.builder()
                .id(101L)
                .name("Ibuprofen")
                .expiryDate(Date.from(LocalDate.now().minusDays(1) // Past expiry
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
    }

    @Test
    void shouldReturnTrueForActiveAllocation() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .pharmacy(pharmacy)
                .drug(activeDrug)
                .allocatedStock(10)
                .build();

        // Act & Assert
        assertThat(PharmacyDrugAllocationDomain.isActive(allocation)).isTrue();
    }

    @Test
    void shouldReturnFalseForExpiredDrug() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .pharmacy(pharmacy)
                .drug(expiredDrug)
                .allocatedStock(10)
                .build();

        // Act & Assert
        assertThat(PharmacyDrugAllocationDomain.isActive(allocation)).isFalse();
    }

    @Test
    void shouldReturnFalseForZeroStock() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .pharmacy(pharmacy)
                .drug(activeDrug)
                .allocatedStock(0)
                .build();

        // Act & Assert
        assertThat(PharmacyDrugAllocationDomain.isActive(allocation)).isFalse();
    }

    @Test
    void shouldReturnFalseForNegativeStock() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .pharmacy(pharmacy)
                .drug(activeDrug)
                .allocatedStock(-5)
                .build();

        // Act & Assert
        assertThat(PharmacyDrugAllocationDomain.isActive(allocation)).isFalse();
    }

    @Test
    void shouldReturnDrugIdCorrectly() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .drug(activeDrug)
                .build();

        // Act
        Long drugId = PharmacyDrugAllocationDomain.getDrugId(allocation);

        // Assert
        assertThat(drugId).isEqualTo(100L);
    }

    @Test
    void shouldReturnPharmacyIdCorrectly() {
        // Arrange
        PharmacyDrugAllocationDomain allocation = PharmacyDrugAllocationDomain.builder()
                .pharmacy(pharmacy)
                .build();

        // Act
        Long pharmacyId = allocation.getPharmacyId();

        // Assert
        assertThat(pharmacyId).isEqualTo(1L);
    }
}