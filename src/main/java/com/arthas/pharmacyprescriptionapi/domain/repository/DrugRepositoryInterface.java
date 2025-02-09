package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DrugRepositoryInterface {
    DrugSchema save(DrugSchema drug);

    Optional<DrugSchema> findByBatchNumber(String batchNumber);

    Page<DrugSchema> findAll(Pageable pageable);
}