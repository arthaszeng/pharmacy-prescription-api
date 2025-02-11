package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.*;
import com.arthas.pharmacyprescriptionapi.domain.service.AuditLogDomainService;
import com.arthas.pharmacyprescriptionapi.domain.service.PatientDomainService;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import com.arthas.pharmacyprescriptionapi.domain.service.PrescriptionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionApplicationServiceTest {

    @Mock
    private PrescriptionDomainService prescriptionDomainService;

    @Mock
    private PharmacyDomainService pharmacyDomainService;

    @Mock
    private PatientDomainService patientDomainService;

    @Mock
    private AuditLogDomainService auditLogDomainService;

    @InjectMocks
    private PrescriptionApplicationService prescriptionApplicationService;

    private PatientDomain mockPatient;
    private PharmacyDomain mockPharmacy;
    private PrescriptionDomain mockPrescription;
    private List<PrescriptionDrugDomain> mockDrugs;

    @BeforeEach
    void setUp() {
        mockPatient = new PatientDomain(1L, "John Doe", "123456789", LocalDate.of(2000, 10, 10), "Male", "+8612345657", null, null, null);
        mockPharmacy = new PharmacyDomain(1L, "Central Pharmacy", List.of(), null, null);
        mockDrugs = List.of(new PrescriptionDrugDomain(null, new DrugDomain(1L, "Ibuprofen", "GSK", "B001", Date.valueOf("2025-10-10"), 100, LocalDateTime.now(), LocalDateTime.now(), false), 2));

        mockPrescription = PrescriptionDomain.builder()
                .id(1L)
                .patient(mockPatient)
                .pharmacy(mockPharmacy)
                .prescriptionDrugs(mockDrugs)
                .status("PENDING")
                .build();
    }

    @Test
    void shouldCreatePrescriptionSuccessfully() {
        // Arrange
        when(patientDomainService.getPatientById(1L)).thenReturn(mockPatient);
        when(pharmacyDomainService.getPharmacyById(1L)).thenReturn(mockPharmacy);
        when(pharmacyDomainService.validateAndAllocateDrugs(mockPharmacy, mockDrugs)).thenReturn(mockDrugs);
        when(prescriptionDomainService.createPrescription(any())).thenReturn(mockPrescription);

        // Act
        PrescriptionDomain result = prescriptionApplicationService.createPrescription(mockPrescription);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("PENDING");

        verify(prescriptionDomainService, times(1)).createPrescription(any());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        // Arrange
        when(patientDomainService.getPatientById(1L)).thenThrow(new NoSuchElementException("Patient not found"));

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.createPrescription(mockPrescription))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Patient not found");

        verifyNoInteractions(pharmacyDomainService, prescriptionDomainService);
    }

    @Test
    void shouldThrowExceptionWhenPharmacyNotFound() {
        // Arrange
        when(patientDomainService.getPatientById(1L)).thenReturn(mockPatient);
        when(pharmacyDomainService.getPharmacyById(1L)).thenThrow(new NoSuchElementException("Pharmacy not found"));

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.createPrescription(mockPrescription))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Pharmacy not found");

        verifyNoInteractions(prescriptionDomainService);
    }

    @Test
    void shouldThrowExceptionWhenDrugsAreInvalid() {
        // Arrange
        when(patientDomainService.getPatientById(1L)).thenReturn(mockPatient);
        when(pharmacyDomainService.getPharmacyById(1L)).thenReturn(mockPharmacy);
        when(pharmacyDomainService.validateAndAllocateDrugs(mockPharmacy, mockDrugs))
                .thenThrow(new IllegalArgumentException("Invalid drugs"));

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.createPrescription(mockPrescription))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid drugs");

        verifyNoInteractions(prescriptionDomainService);
    }

    @Test
    void shouldFulfillPrescriptionSuccessfully() {
        // Arrange
        when(prescriptionDomainService.getPrescriptionById(1L)).thenReturn(mockPrescription);
        when(pharmacyDomainService.getPharmacyById(1L)).thenReturn(mockPharmacy);
        doNothing().when(pharmacyDomainService).validateAndDeductStock(mockPharmacy, mockDrugs);
        when(prescriptionDomainService.fulfillPrescription(mockPrescription)).thenReturn(mockPrescription);

        // Act
        PrescriptionDomain result = prescriptionApplicationService.fulfillPrescription(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("PENDING");

        verify(auditLogDomainService, times(1)).logFulfillSuccess(mockPrescription);
    }

    @Test
    void shouldThrowExceptionWhenPrescriptionIsNotPending() {
        // Arrange
        mockPrescription.setStatus("COMPLETED");
        when(prescriptionDomainService.getPrescriptionById(1L)).thenReturn(mockPrescription);

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.fulfillPrescription(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not pending fulfillment");

        verifyNoInteractions(pharmacyDomainService);
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        // Arrange
        when(prescriptionDomainService.getPrescriptionById(1L)).thenReturn(mockPrescription);
        when(pharmacyDomainService.getPharmacyById(1L)).thenReturn(mockPharmacy);
        doThrow(new IllegalArgumentException("Insufficient stock"))
                .when(pharmacyDomainService)
                .validateAndDeductStock(mockPharmacy, mockDrugs);

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.fulfillPrescription(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");

        verify(auditLogDomainService, times(1)).logFulfillFailure(eq(mockPrescription), anyString());
    }

    @Test
    void shouldLogFailureWhenUnexpectedErrorOccurs() {
        // Arrange
        when(prescriptionDomainService.getPrescriptionById(1L)).thenReturn(mockPrescription);
        when(pharmacyDomainService.getPharmacyById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThatThrownBy(() -> prescriptionApplicationService.fulfillPrescription(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected error");

        verify(auditLogDomainService, times(1)).logFulfillFailure(eq(mockPrescription), anyString());
    }
}