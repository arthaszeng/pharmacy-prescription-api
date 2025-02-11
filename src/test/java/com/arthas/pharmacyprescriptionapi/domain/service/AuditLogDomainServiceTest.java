package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.*;
import com.arthas.pharmacyprescriptionapi.domain.repository.AuditLogRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuditLogDomainServiceTest {

    @Mock
    private AuditLogRepositoryInterface auditLogRepository;

    @InjectMocks
    private AuditLogDomainService auditLogDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PrescriptionDomain createMockPrescription() {
        return PrescriptionDomain.builder()
                .id(100L)
                .patient(PatientDomain.builder().id(10L).build())
                .pharmacy(PharmacyDomain.builder().id(20L).build())
                .prescriptionDrugs(List.of(
                        PrescriptionDrugDomain.builder()
                                .drug(DrugDomain.builder().id(1L).name("Paracetamol").build())
                                .dosage(2)
                                .build(),
                        PrescriptionDrugDomain.builder()
                                .drug(DrugDomain.builder().id(2L).name("Ibuprofen").build())
                                .dosage(1)
                                .build()))
                .createdAt(LocalDateTime.now())
                .status("PENDING")
                .build();
    }

    @Test
    void shouldLogFulfillSuccess() {
        // Arrange
        PrescriptionDomain prescription = createMockPrescription();
        AuditLogSchema expectedSchema = AuditLogDomain.builder()
                .prescriptionId(100L)
                .patientId(10L)
                .pharmacyId(20L)
                .status("SUCCESS")
                .requestedDrugs(List.of(
                        Map.of("drugId", 1L, "name", "Paracetamol", "quantity", 2),
                        Map.of("drugId", 2L, "name", "Ibuprofen", "quantity", 1)))
                .dispensedDrugs(List.of(
                        Map.of("drugId", 1L, "name", "Paracetamol", "quantity", 2),
                        Map.of("drugId", 2L, "name", "Ibuprofen", "quantity", 1)))
                .createdAt(prescription.getCreatedAt())
                .build()
                .toSchema();

        // Act
        auditLogDomainService.logFulfillSuccess(prescription);

        // Assert
        verify(auditLogRepository, times(1)).save(argThat(schema ->
                schema.getPrescriptionId().equals(100L) &&
                        schema.getStatus().equals("SUCCESS") &&
                        schema.getRequestedDrugs().size() == 2 &&
                        schema.getDispensedDrugs().size() == 2
        ));
    }

    @Test
    void shouldLogFulfillFailure() {
        // Arrange
        PrescriptionDomain prescription = createMockPrescription();
        String failureReason = "Stock unavailable";

        AuditLogSchema expectedSchema = AuditLogDomain.builder()
                .prescriptionId(100L)
                .patientId(10L)
                .pharmacyId(20L)
                .status("FAILURE")
                .failureReason(failureReason)
                .requestedDrugs(List.of(
                        Map.of("drugId", 1L, "name", "Paracetamol", "quantity", 2),
                        Map.of("drugId", 2L, "name", "Ibuprofen", "quantity", 1)))
                .dispensedDrugs(List.of())
                .createdAt(prescription.getCreatedAt())
                .build()
                .toSchema();

        // Act
        auditLogDomainService.logFulfillFailure(prescription, failureReason);

        // Assert
        verify(auditLogRepository, times(1)).save(argThat(schema ->
                schema.getPrescriptionId().equals(100L) &&
                        schema.getStatus().equals("FAILURE") &&
                        schema.getFailureReason().equals(failureReason) &&
                        schema.getRequestedDrugs().size() == 2 &&
                        schema.getDispensedDrugs().isEmpty()
        ));
    }

    @Test
    void shouldRetrieveAuditLogsByFilters() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        AuditLogSchema mockSchema = AuditLogSchema.builder()
                .id(1L)
                .prescriptionId(100L)
                .patientId(10L)
                .pharmacyId(20L)
                .status("SUCCESS")
                .requestedDrugs(List.of(Map.of("drugId", 1L, "name", "Paracetamol", "quantity", 2)))
                .dispensedDrugs(List.of(Map.of("drugId", 1L, "name", "Paracetamol", "quantity", 2)))
                .createdAt(LocalDateTime.now())
                .build();

        Page<AuditLogSchema> mockPage = new PageImpl<>(List.of(mockSchema));

        when(auditLogRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        // Act
        Page<AuditLogDomain> result = auditLogDomainService.getAuditLogs(Optional.of(10L), Optional.of(20L), Optional.of("SUCCESS"), pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrescriptionId()).isEqualTo(100L);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("SUCCESS");
        verify(auditLogRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldRetrieveAllAuditLogsWhenNoFiltersApplied() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        AuditLogSchema mockSchema = AuditLogSchema.builder()
                .id(2L)
                .prescriptionId(200L)
                .patientId(30L)
                .pharmacyId(40L)
                .status("FAILURE")
                .requestedDrugs(List.of(Map.of("drugId", 2L, "name", "Ibuprofen", "quantity", 1)))
                .dispensedDrugs(List.of())
                .createdAt(LocalDateTime.now())
                .build();

        Page<AuditLogSchema> mockPage = new PageImpl<>(List.of(mockSchema));

        when(auditLogRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        // Act
        Page<AuditLogDomain> result = auditLogDomainService.getAuditLogs(Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrescriptionId()).isEqualTo(200L);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("FAILURE");
        verify(auditLogRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}