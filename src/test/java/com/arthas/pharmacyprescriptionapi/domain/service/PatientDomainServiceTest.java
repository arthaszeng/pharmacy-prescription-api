package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PatientDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PatientRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PatientDomainServiceTest {

    @Mock
    private PatientRepositoryInterface patientRepository;

    @InjectMocks
    private PatientDomainService patientDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PatientSchema createMockPatientSchema() {
        return PatientSchema.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 20))
                .gender("Male")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldGetPatientByIdSuccessfully() {
        // Arrange
        Long patientId = 1L;
        PatientSchema mockSchema = createMockPatientSchema();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(mockSchema));

        // Act
        PatientDomain result = patientDomainService.getPatientById(patientId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(patientId);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getPhone()).isEqualTo("1234567890");

        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        // Arrange
        Long patientId = 99L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patientDomainService.getPatientById(patientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Patient ID 99 does not exist");

        verify(patientRepository, times(1)).findById(patientId);
    }
}