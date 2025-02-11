package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PrescriptionDomainTest {

    @Test
    void shouldConvertSchemaToDomainSuccessfully() {
        // Arrange
        PrescriptionSchema prescriptionSchema = mock(PrescriptionSchema.class);
        PrescriptionDrugSchema drugSchema = mock(PrescriptionDrugSchema.class);
        DrugSchema drug = mock(DrugSchema.class);

        when(drug.getId()).thenReturn(1L);
        when(drugSchema.getDrug()).thenReturn(drug);
        when(drugSchema.getDosage()).thenReturn(2);

        when(prescriptionSchema.getId()).thenReturn(1L);
        when(prescriptionSchema.getPatient()).thenReturn(mock(PatientSchema.class));
        when(prescriptionSchema.getPharmacy()).thenReturn(mock(PharmacySchema.class));
        when(prescriptionSchema.getPrescriptionDrugs()).thenReturn(List.of(drugSchema));
        when(prescriptionSchema.getStatus()).thenReturn("PENDING");
        when(prescriptionSchema.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(prescriptionSchema.getUpdatedAt()).thenReturn(LocalDateTime.now());

        // Act
        PrescriptionDomain prescriptionDomain = PrescriptionDomain.fromSchema(prescriptionSchema);

        // Assert
        assertThat(prescriptionDomain).isNotNull();
        assertThat(prescriptionDomain.getId()).isEqualTo(1L);
        assertThat(prescriptionDomain.getPrescriptionDrugs()).hasSize(1);
        assertThat(prescriptionDomain.getPrescriptionDrugs().get(0).getDrug().getId()).isEqualTo(1L);
    }
}