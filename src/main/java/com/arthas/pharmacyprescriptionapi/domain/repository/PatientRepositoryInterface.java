package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;

import java.util.Optional;

public interface PatientRepositoryInterface {
    Optional<PatientSchema> findById(Long id);
}
