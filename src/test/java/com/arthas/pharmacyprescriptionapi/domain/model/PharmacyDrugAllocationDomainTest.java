package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class PharmacyDrugAllocationDomainTest {

    private DrugDomain activeDrug;
    private DrugDomain expiredDrug;
    private PharmacyDomain pharmacy;
    private PharmacySchema pharmacySchema;
    private DrugSchema drugSchema;
    private PharmacyDrugAllocationSchema allocationSchema;

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

        pharmacySchema = PharmacySchema.builder()
                .id(1L)
                .name("Test Pharmacy")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        drugSchema = DrugSchema.builder()
                .id(10L)
                .name("Test Drug")
                .manufacturer("Test Manufacturer")
                .batchNumber("BATCH001")
                .expiryDate(new java.util.Date())
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();

        allocationSchema = PharmacyDrugAllocationSchema.builder()
                .id(100L)
                .pharmacy(pharmacySchema)
                .drug(drugSchema)
                .allocatedStock(50)
                .version(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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

    @Test
    void shouldConvertFromSchemaIncludingPharmacy() {
        // Act
        PharmacyDrugAllocationDomain domain = PharmacyDrugAllocationDomain.fromSchema(allocationSchema, true);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(allocationSchema.getId());
        assertThat(domain.getPharmacy()).isNotNull();
        assertThat(domain.getPharmacy().getId()).isEqualTo(pharmacySchema.getId());
        assertThat(domain.getDrug()).isNotNull();
        assertThat(domain.getDrug().getId()).isEqualTo(drugSchema.getId());
        assertThat(domain.getAllocatedStock()).isEqualTo(allocationSchema.getAllocatedStock());
        assertThat(domain.getVersion()).isEqualTo(allocationSchema.getVersion());
        assertThat(domain.getCreatedAt()).isEqualTo(allocationSchema.getCreatedAt());
        assertThat(domain.getUpdatedAt()).isEqualTo(allocationSchema.getUpdatedAt());
    }

    @Test
    void shouldConvertFromSchemaExcludingPharmacy() {
        // Act
        PharmacyDrugAllocationDomain domain = PharmacyDrugAllocationDomain.fromSchema(allocationSchema, false);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(allocationSchema.getId());
        assertThat(domain.getPharmacy()).isNull();
        assertThat(domain.getDrug()).isNotNull();
        assertThat(domain.getDrug().getId()).isEqualTo(drugSchema.getId());
        assertThat(domain.getAllocatedStock()).isEqualTo(allocationSchema.getAllocatedStock());
        assertThat(domain.getVersion()).isEqualTo(allocationSchema.getVersion());
        assertThat(domain.getCreatedAt()).isEqualTo(allocationSchema.getCreatedAt());
        assertThat(domain.getUpdatedAt()).isEqualTo(allocationSchema.getUpdatedAt());
    }

    @Test
    void shouldConvertToSchemaSuccessfully() {
        // Arrange
        PharmacyDrugAllocationDomain domain = PharmacyDrugAllocationDomain.builder()
                .id(200L)
                .pharmacy(PharmacyDomain.fromSchema(pharmacySchema, false))
                .drug(DrugDomain.fromSchema(drugSchema))
                .allocatedStock(30)
                .version(2L)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        // Act
        PharmacyDrugAllocationSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getId()).isEqualTo(domain.getId());
        assertThat(schema.getPharmacy()).isNotNull();
        assertThat(schema.getPharmacy().getId()).isEqualTo(pharmacySchema.getId());
        assertThat(schema.getDrug()).isNotNull();
        assertThat(schema.getDrug().getId()).isEqualTo(drugSchema.getId());
        assertThat(schema.getAllocatedStock()).isEqualTo(domain.getAllocatedStock());
        assertThat(schema.getVersion()).isEqualTo(domain.getVersion());
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }
}