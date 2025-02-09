package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.DrugRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void testAddDrug_Success() {
        DrugDomain drug = new DrugDomain(
                null, "Paracetamol", "XYZ Pharma", "B123",
                new Date(), 100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        DrugSchema savedSchema = drug.toSchema();
        savedSchema.setId(1L);

        when(drugRepository.findByBatchNumber("B123")).thenReturn(Optional.empty());
        when(drugRepository.save(any())).thenReturn(savedSchema);

        DrugDomain savedDrug = drugDomainService.addDrug(drug);

        assertNotNull(savedDrug);
        assertEquals("Paracetamol", savedDrug.getName());
        assertEquals("XYZ Pharma", savedDrug.getManufacturer());
        assertEquals("B123", savedDrug.getBatchNumber());

        ArgumentCaptor<DrugSchema> captor = ArgumentCaptor.forClass(DrugSchema.class);
        verify(drugRepository).save(captor.capture());

        assertEquals("Paracetamol", captor.getValue().getName());
    }

    @Test
    void testAddDrug_BatchNumberExists() {
        DrugDomain drug = new DrugDomain(
                null, "Paracetamol", "XYZ Pharma", "B123",
                new Date(), 100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        when(drugRepository.findByBatchNumber("B123")).thenReturn(Optional.of(new DrugSchema()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> drugDomainService.addDrug(drug));
        assertEquals("Batch number already exists", exception.getMessage());

        verify(drugRepository, never()).save(any());
    }
}