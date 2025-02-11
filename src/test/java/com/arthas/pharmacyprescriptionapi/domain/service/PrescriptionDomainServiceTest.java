package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.*;
import com.arthas.pharmacyprescriptionapi.domain.repository.PrescriptionRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrescriptionDomainServiceTest {

    @Mock
    private PrescriptionRepositoryInterface prescriptionRepository;

    @InjectMocks
    private PrescriptionDomainService prescriptionDomainService;

    private PrescriptionSchema mockPrescriptionSchema;
    private PrescriptionDomain mockPrescriptionDomain;

    @BeforeEach
    void setUp() {
        mockPrescriptionSchema = createMockPrescriptionSchema();
        mockPrescriptionDomain = createMockPrescriptionDomain();
    }

    @Test
    void shouldCreatePrescriptionSuccessfully() {
        when(prescriptionRepository.save(any(PrescriptionSchema.class)))
                .thenReturn(mockPrescriptionSchema);

        PrescriptionDomain result = prescriptionDomainService.createPrescription(mockPrescriptionDomain);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockPrescriptionSchema.getId());
        assertThat(result.getPatient().getId()).isEqualTo(mockPrescriptionSchema.getPatient().getId());
        assertThat(result.getPharmacy().getId()).isEqualTo(mockPrescriptionSchema.getPharmacy().getId());

        verify(prescriptionRepository).save(any(PrescriptionSchema.class));
    }

    @Test
    void shouldThrowExceptionWhenPrescriptionNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prescriptionDomainService.getPrescriptionById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Prescription ID 1 does not exist.");

        verify(prescriptionRepository).findById(1L);
    }

    private PrescriptionSchema createMockPrescriptionSchema() {
        return PrescriptionSchema.builder()
                .id(1L)
                .patient(createMockPatientSchema())
                .pharmacy(createMockPharmacySchema())
                .prescriptionDrugs(Collections.singletonList(createMockPrescriptionDrugSchema()))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PrescriptionDomain createMockPrescriptionDomain() {
        return PrescriptionDomain.builder()
                .id(1L)
                .patient(createMockPatientDomain())
                .pharmacy(createMockPharmacyDomain())
                .prescriptionDrugs(Collections.singletonList(createMockPrescriptionDrugDomain()))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PatientSchema createMockPatientSchema() {
        return PatientSchema.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now())  // FIXED: Using Date instead of LocalDate
                .gender("Male")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PatientDomain createMockPatientDomain() {
        return PatientDomain.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now()) // FIXED: Using Date instead of LocalDate
                .gender("Male")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PharmacySchema createMockPharmacySchema() {
        return PharmacySchema.builder()
                .id(1L)
                .name("Central Pharmacy")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PharmacyDomain createMockPharmacyDomain() {
        return PharmacyDomain.builder()
                .id(1L)
                .name("Central Pharmacy")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PrescriptionDrugSchema createMockPrescriptionDrugSchema() {
        return PrescriptionDrugSchema.builder()
                .id(1L)
                .drug(createMockDrugSchema())
                .dosage(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PrescriptionDrugDomain createMockPrescriptionDrugDomain() {
        return PrescriptionDrugDomain.builder()
                .id(1L)
                .drug(createMockDrugDomain())
                .dosage(2)
                .build();
    }

    private DrugSchema createMockDrugSchema() {
        return DrugSchema.builder()
                .id(1L)
                .name("Aspirin")
                .manufacturer("Bayer")
                .batchNumber("BAY001")
                .expiryDate(new Date()) // FIXED: Using Date instead of LocalDate
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    private DrugDomain createMockDrugDomain() {
        return DrugDomain.builder()
                .id(1L)
                .name("Aspirin")
                .manufacturer("Bayer")
                .batchNumber("BAY001")
                .expiryDate(new Date()) // FIXED: Using Date instead of LocalDate
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }
}