package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PharmacyRepositoryInterface {
    Page<PharmacySchema> findAll(Pageable pageable);

    Optional<PharmacySchema> findById(Long id);
}