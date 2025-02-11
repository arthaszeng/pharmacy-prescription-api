package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.*;
import com.arthas.pharmacyprescriptionapi.domain.repository.PrescriptionRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionDomainServiceTest {

    @Mock
    private PrescriptionRepositoryInterface prescriptionRepository;

    @InjectMocks
    private PrescriptionDomainService prescriptionDomainService;

    private PrescriptionDomain mockPrescription;
    private PrescriptionSchema mockPrescriptionSchema;

    @BeforeEach
    void setUp() {
        // Mock PrescriptionDomain

        mockPrescription = PrescriptionDomain.builder()
                .id(1L)
                .patient(PatientDomain.builder().id(101L).build())
                .pharmacy(PharmacyDomain.builder().id(201L).build())
                .status("PENDING")
                .prescriptionDrugs(List.of(
                        PrescriptionDrugDomain.builder()
                                .drug(DrugDomain.builder().deleted(false).build())
                                .dosage(2)
                                .build()
                ))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Mock PrescriptionSchema (database entity)
        mockPrescriptionSchema = PrescriptionSchema.builder()
                .id(1L)
                .patient(PatientSchema.builder().id(101L).build())
                .pharmacy(PharmacySchema.builder().id(201L).build())
                .status("PENDING")
                .prescriptionDrugs(List.of(
                        PrescriptionDrugSchema.builder()
                                .drug(DrugSchema.builder().deleted(false).build())
                                .dosage(2)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Test: Successfully create a prescription.
     */
    @Test
    void createPrescription_shouldSaveSuccessfully() {
        when(prescriptionRepository.save(any(PrescriptionSchema.class)))
                .thenReturn(mockPrescriptionSchema);

        PrescriptionDomain result = prescriptionDomainService.createPrescription(mockPrescription);

        assertThat(result.getId()).isEqualTo(mockPrescription.getId());
        assertThat(result.getStatus()).isEqualTo("PENDING");

        verify(prescriptionRepository, times(1)).save(any(PrescriptionSchema.class));
    }

    /**
     * Test: `createPrescription` should throw exception on data integrity violation.
     */
    @Test
    void createPrescription_shouldThrowExceptionOnDataIntegrityViolation() {
        when(prescriptionRepository.save(any(PrescriptionSchema.class)))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violated"));

        assertThatThrownBy(() -> prescriptionDomainService.createPrescription(mockPrescription))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to save prescription due to data integrity violation");

        verify(prescriptionRepository, times(1)).save(any(PrescriptionSchema.class));
    }

    /**
     * Test: `createPrescription` should throw exception on entity not found.
     */
    @Test
    void createPrescription_shouldThrowExceptionOnEntityNotFound() {
        when(prescriptionRepository.save(any(PrescriptionSchema.class)))
                .thenThrow(new EntityNotFoundException("Doctor ID not found"));

        assertThatThrownBy(() -> prescriptionDomainService.createPrescription(mockPrescription))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Referenced entity not found");

        verify(prescriptionRepository, times(1)).save(any(PrescriptionSchema.class));
    }

    /**
     * Test: `createPrescription` should handle generic exceptions.
     */
    @Test
    void createPrescription_shouldHandleGenericException() {
        when(prescriptionRepository.save(any(PrescriptionSchema.class)))
                .thenThrow(new RuntimeException("Unexpected database error"));

        assertThatThrownBy(() -> prescriptionDomainService.createPrescription(mockPrescription))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("An unexpected error occurred while creating the prescription");

        verify(prescriptionRepository, times(1)).save(any(PrescriptionSchema.class));
    }

    /**
     * Test: Successfully fulfill a prescription.
     */
    @Test
    void fulfillPrescription_shouldUpdateStatusToFulfilled() {
        PrescriptionSchema fulfilledSchema = mockPrescriptionSchema;
        mockPrescriptionSchema.setStatus("FULFILLED");
        mockPrescriptionSchema.setUpdatedAt(LocalDateTime.now());

        when(prescriptionRepository.save(any(PrescriptionSchema.class))).thenReturn(fulfilledSchema);

        PrescriptionDomain result = prescriptionDomainService.fulfillPrescription(mockPrescription);

        assertThat(result.getStatus()).isEqualTo("FULFILLED");

        verify(prescriptionRepository, times(1)).save(any(PrescriptionSchema.class));
    }

    /**
     * Test: Successfully get a prescription by ID.
     */
    @Test
    void getPrescriptionById_shouldReturnPrescriptionIfExists() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(mockPrescriptionSchema));

        PrescriptionDomain result = prescriptionDomainService.getPrescriptionById(1L);

        assertThat(result.getId()).isEqualTo(mockPrescription.getId());
        assertThat(result.getStatus()).isEqualTo("PENDING");

        verify(prescriptionRepository, times(1)).findById(1L);
    }

    /**
     * Test: `getPrescriptionById()` should throw exception if prescription does not exist.
     */
    @Test
    void getPrescriptionById_shouldThrowExceptionIfNotExists() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prescriptionDomainService.getPrescriptionById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Prescription ID 1 does not exist.");

        verify(prescriptionRepository, times(1)).findById(1L);
    }
}