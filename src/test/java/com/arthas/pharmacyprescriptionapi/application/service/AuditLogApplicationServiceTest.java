package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.AuditLogDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.AuditLogDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogApplicationServiceTest {

    @Mock
    private AuditLogDomainService auditLogDomainService;

    @InjectMocks
    private AuditLogApplicationService auditLogApplicationService;

    private Page<AuditLogDomain> mockPage;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        AuditLogDomain log1 = AuditLogDomain.builder()
                .prescriptionId(1L)
                .patientId(10L)
                .pharmacyId(20L)
                .status("SUCCESS")
                .build();

        AuditLogDomain log2 = AuditLogDomain.builder()
                .prescriptionId(2L)
                .patientId(11L)
                .pharmacyId(21L)
                .status("FAILURE")
                .build();

        List<AuditLogDomain> logs = List.of(log1, log2);
        pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        mockPage = new PageImpl<>(logs, pageable, logs.size());
    }

    @Test
    void shouldCallDomainServiceWithCorrectParameters() {
        when(auditLogDomainService.getAuditLogs(Optional.of(10L), Optional.of(20L), Optional.of("SUCCESS"), pageable))
                .thenReturn(mockPage);

        Page<AuditLogDomain> result = auditLogApplicationService.getAuditLogs(10L, 20L, "SUCCESS", pageable);

        assertThat(result).isEqualTo(mockPage);
        verify(auditLogDomainService, times(1)).getAuditLogs(Optional.of(10L), Optional.of(20L), Optional.of("SUCCESS"), pageable);
    }

    @Test
    void shouldCallDomainServiceWithEmptyFiltersWhenNoParametersProvided() {
        when(auditLogDomainService.getAuditLogs(Optional.empty(), Optional.empty(), Optional.empty(), pageable))
                .thenReturn(mockPage);

        Page<AuditLogDomain> result = auditLogApplicationService.getAuditLogs(null, null, null, pageable);

        assertThat(result).isEqualTo(mockPage);
        verify(auditLogDomainService, times(1)).getAuditLogs(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void shouldCallDomainServiceWithOnlyPatientId() {
        when(auditLogDomainService.getAuditLogs(Optional.of(10L), Optional.empty(), Optional.empty(), pageable))
                .thenReturn(mockPage);

        auditLogApplicationService.getAuditLogs(10L, null, null, pageable);

        verify(auditLogDomainService, times(1)).getAuditLogs(Optional.of(10L), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void shouldCallDomainServiceWithOnlyPharmacyId() {
        when(auditLogDomainService.getAuditLogs(Optional.empty(), Optional.of(20L), Optional.empty(), pageable))
                .thenReturn(mockPage);

        auditLogApplicationService.getAuditLogs(null, 20L, null, pageable);

        verify(auditLogDomainService, times(1)).getAuditLogs(Optional.empty(), Optional.of(20L), Optional.empty(), pageable);
    }

    @Test
    void shouldCallDomainServiceWithOnlyStatus() {
        when(auditLogDomainService.getAuditLogs(Optional.empty(), Optional.empty(), Optional.of("SUCCESS"), pageable))
                .thenReturn(mockPage);

        auditLogApplicationService.getAuditLogs(null, null, "SUCCESS", pageable);

        verify(auditLogDomainService, times(1)).getAuditLogs(Optional.empty(), Optional.empty(), Optional.of("SUCCESS"), pageable);
    }
}