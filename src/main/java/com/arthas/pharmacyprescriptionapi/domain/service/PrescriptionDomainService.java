package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PrescriptionRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionDomainService {
    private final PrescriptionRepositoryInterface prescriptionRepository;

    @Transactional
    public PrescriptionDomain createPrescription(PrescriptionDomain prescription) {
        PrescriptionSchema saved = prescriptionRepository.save(prescription.toSchema());
        return PrescriptionDomain.fromSchema(saved);
    }
}