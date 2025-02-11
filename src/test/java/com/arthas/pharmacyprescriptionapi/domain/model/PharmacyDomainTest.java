package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PharmacyDomainTest {

    @Test
    void shouldConvertSchemaToDomainWithoutAllocations() {
        // Arrange
        PharmacySchema schema = PharmacySchema.builder()
                .id(2L)
                .name("Downtown Pharmacy")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .allocations(List.of(new PharmacyDrugAllocationSchema()))
                .build();

        // Act
        PharmacyDomain domain = PharmacyDomain.fromSchema(schema, false);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(2L);
        assertThat(domain.getName()).isEqualTo("Downtown Pharmacy");
        assertThat(domain.getAllocations()).isNull();
        assertThat(domain.getCreatedAt()).isNotNull();
        assertThat(domain.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingFromSchema() {
        // Arrange
        PharmacySchema schema = new PharmacySchema();

        // Act
        PharmacyDomain domain = PharmacyDomain.fromSchema(schema, true);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isNull();
        assertThat(domain.getName()).isNull();
        assertThat(domain.getAllocations()).isEmpty();
        assertThat(domain.getCreatedAt()).isNotNull();
        assertThat(domain.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldHandleNullValuesWhenConvertingToSchema() {
        // Arrange
        PharmacyDomain domain = PharmacyDomain.builder().build();

        // Act
        PharmacySchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getAllocations()).isEmpty();
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }
}