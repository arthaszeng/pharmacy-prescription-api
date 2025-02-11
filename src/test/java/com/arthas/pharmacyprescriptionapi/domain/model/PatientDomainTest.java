package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PatientDomainTest {

    @Test
    void shouldConvertSchemaToDomainSuccessfully() {
        // Arrange
        PatientSchema schema = new PatientSchema(
                1L, "John", "Doe", LocalDate.of(1985, 6, 15),
                "Male", "1234567890", "john.doe@example.com",
                LocalDateTime.now(), LocalDateTime.now()
        );

        // Act
        PatientDomain domain = PatientDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getFirstName()).isEqualTo("John");
        assertThat(domain.getLastName()).isEqualTo("Doe");
        assertThat(domain.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 6, 15));
        assertThat(domain.getGender()).isEqualTo("Male");
        assertThat(domain.getPhone()).isEqualTo("1234567890");
        assertThat(domain.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(domain.getCreatedAt()).isNotNull();
        assertThat(domain.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldConvertDomainToSchemaSuccessfully() {
        // Arrange
        PatientDomain domain = new PatientDomain(
                2L, "Jane", "Smith", LocalDate.of(1992, 3, 22),
                "Female", "9876543210", "jane.smith@example.com",
                LocalDateTime.now(), LocalDateTime.now()
        );

        // Act
        PatientSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getId()).isEqualTo(2L);
        assertThat(schema.getFirstName()).isEqualTo("Jane");
        assertThat(schema.getLastName()).isEqualTo("Smith");
        assertThat(schema.getDateOfBirth()).isEqualTo(LocalDate.of(1992, 3, 22));
        assertThat(schema.getGender()).isEqualTo("Female");
        assertThat(schema.getPhone()).isEqualTo("9876543210");
        assertThat(schema.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingFromSchema() {
        // Arrange
        PatientSchema schema = new PatientSchema(
                null, null, null, null,
                null, null, null, null, null
        );

        // Act
        PatientDomain domain = PatientDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isNull();
        assertThat(domain.getFirstName()).isNull();
        assertThat(domain.getLastName()).isNull();
        assertThat(domain.getDateOfBirth()).isNull();
        assertThat(domain.getGender()).isNull();
        assertThat(domain.getPhone()).isNull();
        assertThat(domain.getEmail()).isNull();
        assertThat(domain.getCreatedAt()).isNull();
        assertThat(domain.getUpdatedAt()).isNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingToSchema() {
        // Arrange
        PatientDomain domain = new PatientDomain(
                null, null, null, null,
                null, null, null, null, null
        );

        // Act
        PatientSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }
}