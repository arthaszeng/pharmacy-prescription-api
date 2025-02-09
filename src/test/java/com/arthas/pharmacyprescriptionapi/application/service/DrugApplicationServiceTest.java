package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.DrugDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DrugApplicationServiceTest {

    @Mock
    private DrugDomainService drugDomainService;

    @InjectMocks
    private DrugApplicationService drugApplicationService;

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

        DrugDomain savedDrug = new DrugDomain(
                1L, "Paracetamol", "XYZ Pharma", "B123",
                new Date(), 100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        when(drugDomainService.addDrug(any())).thenReturn(savedDrug);

        DrugDomain result = drugApplicationService.addDrug(drug);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getName());
        assertEquals("B123", result.getBatchNumber());

        verify(drugDomainService, times(1)).addDrug(any());
    }
}