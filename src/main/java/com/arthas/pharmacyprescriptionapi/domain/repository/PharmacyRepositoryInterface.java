package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;

import java.util.List;

public interface PharmacyRepositoryInterface {
    List<PharmacySchema> findAll();
}