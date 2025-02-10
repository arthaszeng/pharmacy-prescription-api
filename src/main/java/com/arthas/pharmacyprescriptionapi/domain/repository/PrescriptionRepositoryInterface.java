package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionSchema;

import java.util.Optional;

public interface PrescriptionRepositoryInterface {
    PrescriptionSchema save(PrescriptionSchema prescription);

    Optional<PrescriptionSchema> findById(Long id);
}