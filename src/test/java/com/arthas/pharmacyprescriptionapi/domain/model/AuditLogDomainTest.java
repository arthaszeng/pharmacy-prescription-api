package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.AuditLogSchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuditLogDomainTest {

    @Test
    void shouldConvertSchemaToDomainSuccessfully() {
        // Arrange
        AuditLogSchema schema = AuditLogSchema.builder()
                .id(1L)
                .prescriptionId(101L)
                .patientId(202L)
                .pharmacyId(303L)
                .status("SUCCESS")
                .failureReason(null)
                .requestedDrugs(List.of(Map.of("drugId", 1, "quantity", 2)))
                .dispensedDrugs(List.of(Map.of("drugId", 1, "quantity", 2)))
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        AuditLogDomain domain = AuditLogDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getPrescriptionId()).isEqualTo(101L);
        assertThat(domain.getPatientId()).isEqualTo(202L);
        assertThat(domain.getPharmacyId()).isEqualTo(303L);
        assertThat(domain.getStatus()).isEqualTo("SUCCESS");
        assertThat(domain.getFailureReason()).isNull();
        assertThat(domain.getRequestedDrugs()).hasSize(1);
        assertThat(domain.getDispensedDrugs()).hasSize(1);
    }

    @Test
    void shouldConvertDomainToSchemaSuccessfully() {
        // Arrange
        AuditLogDomain domain = AuditLogDomain.builder()
                .id(1L)
                .prescriptionId(101L)
                .patientId(202L)
                .pharmacyId(303L)
                .status("FAILURE")
                .failureReason("Stock unavailable")
                .requestedDrugs(List.of(Map.of("drugId", 2, "quantity", 1)))
                .dispensedDrugs(List.of())
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        AuditLogSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getId()).isEqualTo(1L);
        assertThat(schema.getPrescriptionId()).isEqualTo(101L);
        assertThat(schema.getPatientId()).isEqualTo(202L);
        assertThat(schema.getPharmacyId()).isEqualTo(303L);
        assertThat(schema.getStatus()).isEqualTo("FAILURE");
        assertThat(schema.getFailureReason()).isEqualTo("Stock unavailable");
        assertThat(schema.getRequestedDrugs()).hasSize(1);
        assertThat(schema.getDispensedDrugs()).isEmpty();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingFromSchema() {
        // Arrange
        AuditLogSchema schema = AuditLogSchema.builder()
                .id(1L)
                .prescriptionId(101L)
                .patientId(202L)
                .pharmacyId(303L)
                .status("SUCCESS")
                .failureReason(null)
                .requestedDrugs(null)
                .dispensedDrugs(null)
                .createdAt(null)
                .build();

        // Act
        AuditLogDomain domain = AuditLogDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getFailureReason()).isNull();
        assertThat(domain.getRequestedDrugs()).isNull();
        assertThat(domain.getDispensedDrugs()).isNull();
        assertThat(domain.getCreatedAt()).isNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingToSchema() {
        // Arrange
        AuditLogDomain domain = AuditLogDomain.builder()
                .id(1L)
                .prescriptionId(101L)
                .patientId(202L)
                .pharmacyId(303L)
                .status("SUCCESS")
                .failureReason(null)
                .requestedDrugs(null)
                .dispensedDrugs(null)
                .createdAt(null)
                .build();

        // Act
        AuditLogSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getFailureReason()).isNull();
        assertThat(schema.getRequestedDrugs()).isNull();
        assertThat(schema.getDispensedDrugs()).isNull();
        assertThat(schema.getCreatedAt()).isNull();
    }
}