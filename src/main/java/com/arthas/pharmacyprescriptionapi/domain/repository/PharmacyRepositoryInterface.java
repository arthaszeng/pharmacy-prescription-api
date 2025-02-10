package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PharmacyRepositoryInterface {
    Page<PharmacySchema> findAll(Pageable pageable);
}