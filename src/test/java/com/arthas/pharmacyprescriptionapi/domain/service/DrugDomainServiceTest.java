package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.DrugRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DrugDomainServiceTest {

    @Mock
    private DrugRepositoryInterface drugRepository;

    @InjectMocks
    private DrugDomainService drugDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DrugDomain createMockDrugDomain() {
        return DrugDomain.builder()
                .id(1L)
                .name("Paracetamol")
                .manufacturer("GSK")
                .batchNumber("GSK001")
                .expiryDate(new Date())
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    private DrugSchema createMockDrugSchema() {
        return DrugSchema.builder()
                .id(1L)
                .name("Paracetamol")
                .manufacturer("GSK")
                .batchNumber("GSK001")
                .expiryDate(new Date())
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    @Test
    void shouldAddNewDrugSuccessfully() {
        // Arrange
        DrugDomain drug = createMockDrugDomain();
        DrugSchema savedSchema = createMockDrugSchema();

        when(drugRepository.findByBatchNumber(drug.getBatchNumber())).thenReturn(Optional.empty());
        when(drugRepository.save(any(DrugSchema.class))).thenReturn(savedSchema);

        // Act
        DrugDomain result = drugDomainService.addDrug(drug);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Paracetamol");
        assertThat(result.getBatchNumber()).isEqualTo("GSK001");

        verify(drugRepository, times(1)).findByBatchNumber(drug.getBatchNumber());
        verify(drugRepository, times(1)).save(any(DrugSchema.class));
    }

    @Test
    void shouldThrowExceptionWhenBatchNumberAlreadyExists() {
        // Arrange
        DrugDomain drug = createMockDrugDomain();
        when(drugRepository.findByBatchNumber(drug.getBatchNumber())).thenReturn(Optional.of(createMockDrugSchema()));

        // Act & Assert
        assertThatThrownBy(() -> drugDomainService.addDrug(drug))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Batch number already exists");

        verify(drugRepository, times(1)).findByBatchNumber(drug.getBatchNumber());
        verify(drugRepository, never()).save(any(DrugSchema.class));
    }

    @Test
    void shouldGetAllDrugsSuccessfully() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DrugSchema> mockPage = new PageImpl<>(java.util.List.of(createMockDrugSchema()));

        when(drugRepository.findAll(pageable)).thenReturn(mockPage);

        // Act
        Page<DrugDomain> result = drugDomainService.getAllDrugs(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Paracetamol");

        verify(drugRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoDrugsAvailable() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DrugSchema> emptyPage = Page.empty();

        when(drugRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<DrugDomain> result = drugDomainService.getAllDrugs(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();

        verify(drugRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldGetDrugByIdSuccessfully() {
        // Arrange
        Long drugId = 1L;
        DrugSchema mockSchema = createMockDrugSchema();
        when(drugRepository.findById(drugId)).thenReturn(Optional.of(mockSchema));

        // Act
        DrugDomain result = drugDomainService.getDrugById(drugId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Paracetamol");

        verify(drugRepository, times(1)).findById(drugId);
    }

    @Test
    void shouldThrowExceptionWhenDrugNotFoundById() {
        // Arrange
        Long drugId = 99L;
        when(drugRepository.findById(drugId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> drugDomainService.getDrugById(drugId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Drug ID 99 does not exist");

        verify(drugRepository, times(1)).findById(drugId);
    }
}