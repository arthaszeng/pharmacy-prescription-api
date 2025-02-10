package com.arthas.pharmacyprescriptionapi.domain.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PatientDomain;
import com.arthas.pharmacyprescriptionapi.domain.repository.PatientRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientDomainService {
    private final PatientRepositoryInterface patientRepository;

    public PatientDomain getPatientById(Long id) {
        PatientSchema schema = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " does not exist"));
        return PatientDomain.fromSchema(schema);
    }
}
