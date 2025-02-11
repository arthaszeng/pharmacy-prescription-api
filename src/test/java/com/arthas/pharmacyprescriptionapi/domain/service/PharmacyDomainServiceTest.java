package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PharmacyDomainServiceTest {

    @Mock
    private PharmacyRepositoryInterface pharmacyRepository;

    @InjectMocks
    private PharmacyDomainService pharmacyDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetPharmacyByIdSuccessfully() {
        // Arrange
        Long pharmacyId = 1L;
        PharmacySchema mockSchema = createMockPharmacySchema();

        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(mockSchema));

        // Act
        PharmacyDomain result = pharmacyDomainService.getPharmacyById(pharmacyId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(pharmacyId);
        assertThat(result.getName()).isEqualTo(mockSchema.getName());

        verify(pharmacyRepository, times(1)).findById(pharmacyId);
    }

    @Test
    void shouldThrowExceptionWhenPharmacyNotFound() {
        // Arrange
        Long pharmacyId = 999L;
        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NoSuchElementException.class,
                () -> pharmacyDomainService.getPharmacyById(pharmacyId),
                "Pharmacy ID " + pharmacyId + " does not exist."
        );

        verify(pharmacyRepository, times(1)).findById(pharmacyId);
    }

    private PharmacySchema createMockPharmacySchema() {
        return PharmacySchema.builder()
                .id(1L)
                .name("Central Pharmacy")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}