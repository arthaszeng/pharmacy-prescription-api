package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.DrugDomainService;
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
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrugApplicationServiceTest {

    @Mock
    private DrugDomainService drugDomainService;

    @InjectMocks
    private DrugApplicationService drugApplicationService;

    private DrugDomain sampleDrug;

    @BeforeEach
    void setUp() {
        sampleDrug = DrugDomain.builder()
                .id(1L)
                .name("Paracetamol")
                .manufacturer("GSK")
                .batchNumber("GSK001")
                .expiryDate(new Date(2025, 12, 31))
                .stock(100)
                .build();
    }

    @Test
    void shouldAddDrugSuccessfully() {
        // Arrange
        when(drugDomainService.addDrug(sampleDrug)).thenReturn(sampleDrug);

        // Act
        DrugDomain result = drugApplicationService.addDrug(sampleDrug);

        // Assert
        assertThat(result).isEqualTo(sampleDrug);
        verify(drugDomainService, times(1)).addDrug(sampleDrug);
    }

    @Test
    void shouldThrowExceptionWhenAddingNullDrug() {
        // Arrange
        doThrow(new IllegalArgumentException("Drug cannot be null"))
                .when(drugDomainService).addDrug(null);

        // Act & Assert
        assertThatThrownBy(() -> drugApplicationService.addDrug(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Drug cannot be null");

        verify(drugDomainService, times(1)).addDrug(null);
    }

    @Test
    void shouldRetrieveAllDrugsSuccessfully() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DrugDomain> drugPage = new PageImpl<>(List.of(sampleDrug));
        when(drugDomainService.getAllDrugs(pageable)).thenReturn(drugPage);

        // Act
        Page<DrugDomain> result = drugApplicationService.getAllDrugs(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(sampleDrug);
        verify(drugDomainService, times(1)).getAllDrugs(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoDrugsAvailable() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DrugDomain> emptyPage = new PageImpl<>(Collections.emptyList());
        when(drugDomainService.getAllDrugs(pageable)).thenReturn(emptyPage);

        // Act
        Page<DrugDomain> result = drugApplicationService.getAllDrugs(pageable);

        // Assert
        assertThat(result.getContent()).isEmpty();
        verify(drugDomainService, times(1)).getAllDrugs(pageable);
    }
}