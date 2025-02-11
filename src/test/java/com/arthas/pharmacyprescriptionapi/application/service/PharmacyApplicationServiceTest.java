package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PharmacyApplicationServiceTest {

    @Mock
    private PharmacyDomainService pharmacyDomainService;

    @InjectMocks
    private PharmacyApplicationService pharmacyApplicationService;

    private Pageable pageable;
    private Page<PharmacyDomain> mockPage;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);
        mockPage = new PageImpl<>(
                List.of(new PharmacyDomain(1L, "Central Pharmacy", Collections.emptyList(), null, null)),
                pageable,
                1
        );
    }

    @Test
    void shouldReturnPaginatedPharmacies() {
        // Arrange
        when(pharmacyDomainService.getAllPharmacies(pageable)).thenReturn(mockPage);

        // Act
        Page<PharmacyDomain> result = pharmacyApplicationService.getAllPharmacies(0, 5);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Central Pharmacy");

        verify(pharmacyDomainService, times(1)).getAllPharmacies(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoPharmaciesExist() {
        // Arrange
        when(pharmacyDomainService.getAllPharmacies(pageable)).thenReturn(Page.empty(pageable));

        // Act
        Page<PharmacyDomain> result = pharmacyApplicationService.getAllPharmacies(0, 5);

        // Assert
        assertThat(result).isNotNull().isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);

        verify(pharmacyDomainService, times(1)).getAllPharmacies(pageable);
    }

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {
        // Act & Assert
        assertThatThrownBy(() -> pharmacyApplicationService.getAllPharmacies(-1, 5))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(pharmacyDomainService);
    }

    @Test
    void shouldThrowExceptionWhenSizeIsZeroOrNegative() {
        // Act & Assert
        assertThatThrownBy(() -> pharmacyApplicationService.getAllPharmacies(0, 0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> pharmacyApplicationService.getAllPharmacies(0, -5))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(pharmacyDomainService);
    }
}