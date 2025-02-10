package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PrescriptionRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionSchema;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionDomainService {
    private final PrescriptionRepositoryInterface prescriptionRepository;

    @Transactional
    public PrescriptionDomain createPrescription(PrescriptionDomain prescription) {
        try {
            PrescriptionSchema saved = prescriptionRepository.save(prescription.toSchema());
            return PrescriptionDomain.fromSchema(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save prescription due to data integrity violation: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Referenced entity not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating the prescription", e);
        }
    }
}