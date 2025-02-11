package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionDrugSchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PrescriptionDrugDomainTest {

    @Test
    void shouldConvertSchemaToDomainSuccessfully() {
        // Arrange
        DrugSchema drugSchema = mock(DrugSchema.class);
        when(drugSchema.getId()).thenReturn(1L);
        when(drugSchema.getName()).thenReturn("Paracetamol");

        PrescriptionDrugSchema schema = PrescriptionDrugSchema.builder()
                .id(10L)
                .drug(drugSchema)
                .dosage(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act
        PrescriptionDrugDomain domain = PrescriptionDrugDomain.fromSchema(schema);

        // Assert
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(10L);
        assertThat(domain.getDrug()).isNotNull();
        assertThat(domain.getDrug().getId()).isEqualTo(1L);
        assertThat(domain.getDosage()).isEqualTo(5);
    }

    @Test
    void shouldConvertDomainToSchemaSuccessfully() {
        // Arrange
        DrugDomain drugDomain = mock(DrugDomain.class);
        when(drugDomain.toSchema()).thenReturn(mock(DrugSchema.class));

        PrescriptionDrugDomain domain = PrescriptionDrugDomain.builder()
                .id(20L)
                .drug(drugDomain)
                .dosage(10)
                .build();

        // Act
        PrescriptionDrugSchema schema = domain.toSchema();

        // Assert
        assertThat(schema).isNotNull();
        assertThat(schema.getId()).isEqualTo(20L);
        assertThat(schema.getDrug()).isNotNull();
        assertThat(schema.getDosage()).isEqualTo(10);
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldReturnCorrectDrugId() {
        // Arrange
        DrugDomain drugDomain = mock(DrugDomain.class);
        when(drugDomain.getId()).thenReturn(99L);

        PrescriptionDrugDomain domain = PrescriptionDrugDomain.builder()
                .id(30L)
                .drug(drugDomain)
                .dosage(15)
                .build();

        // Act
        Long drugId = domain.getDrugId();

        // Assert
        assertThat(drugId).isEqualTo(99L);
    }

    @Test
    void shouldThrowExceptionWhenDrugIsNullInGetDrugId() {
        // Arrange
        PrescriptionDrugDomain domain = PrescriptionDrugDomain.builder()
                .id(40L)
                .drug(null)
                .dosage(20)
                .build();

        // Act & Assert
        assertThat(domain.getDrug()).isNull();
        assertThatThrownBy(domain::getDrugId)
                .isInstanceOf(NullPointerException.class);
    }
}