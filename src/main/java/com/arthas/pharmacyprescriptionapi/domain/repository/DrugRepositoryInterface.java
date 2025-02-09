package com.arthas.pharmacyprescriptionapi.domain.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import java.util.Optional;

public interface DrugRepositoryInterface {
    DrugSchema save(DrugSchema drug);
    Optional<DrugSchema> findByBatchNumber(String batchNumber);
}