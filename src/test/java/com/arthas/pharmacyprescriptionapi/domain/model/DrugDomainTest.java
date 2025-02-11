package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DrugDomainTest {

    @Test
    void shouldConvertSchemaToDomainSuccessfully() {
        // Arrange
        DrugSchema schema = new DrugSchema(
                1L, "Paracetamol", "GSK", "B123",
                new Date(System.currentTimeMillis() + 100000), // Future expiry
                50, LocalDateTime.now(), LocalDateTime.now(), false
        );

        // Act
        DrugDomain domain = DrugDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("Paracetamol");
        assertThat(domain.getManufacturer()).isEqualTo("GSK");
        assertThat(domain.getBatchNumber()).isEqualTo("B123");
        assertThat(domain.getStock()).isEqualTo(50);
        assertThat(domain.isDeleted()).isFalse();
    }

    @Test
    void shouldConvertDomainToSchemaSuccessfully() {
        // Arrange
        DrugDomain domain = new DrugDomain(
                2L, "Ibuprofen", "Bayer", "B456",
                new Date(System.currentTimeMillis() + 100000), // Future expiry
                100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        // Act
        DrugSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getId()).isEqualTo(2L);
        assertThat(schema.getName()).isEqualTo("Ibuprofen");
        assertThat(schema.getManufacturer()).isEqualTo("Bayer");
        assertThat(schema.getBatchNumber()).isEqualTo("B456");
        assertThat(schema.getStock()).isEqualTo(100);
        assertThat(schema.getDeleted()).isFalse();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingFromSchema() {
        // Arrange
        DrugSchema schema = new DrugSchema(
                null, null, null, null,
                null, 0, null, null, false
        );

        // Act
        DrugDomain domain = DrugDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isNull();
        assertThat(domain.getName()).isNull();
        assertThat(domain.getManufacturer()).isNull();
        assertThat(domain.getBatchNumber()).isNull();
        assertThat(domain.getExpiryDate()).isNull();
        assertThat(domain.getCreatedAt()).isNull();
        assertThat(domain.getUpdatedAt()).isNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingToSchema() {
        // Arrange
        DrugDomain domain = new DrugDomain(
                null, null, null, null,
                null, 0, null, null, false
        );

        // Act
        DrugSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldReturnTrueWhenDrugIsExpired() {
        // Arrange
        DrugDomain domain = DrugDomain.builder()
                .expiryDate(new Date(System.currentTimeMillis() - 100000)) // Past expiry
                .build();

        // Act & Assert
        assertThat(domain.isExpired()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDrugIsNotExpired() {
        // Arrange
        DrugDomain domain = DrugDomain.builder()
                .expiryDate(new Date(System.currentTimeMillis() + 100000)) // Future expiry
                .build();

        // Act & Assert
        assertThat(domain.isExpired()).isFalse();
    }
}