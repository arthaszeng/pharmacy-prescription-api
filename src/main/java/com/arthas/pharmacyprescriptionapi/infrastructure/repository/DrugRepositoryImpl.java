package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import com.arthas.pharmacyprescriptionapi.domain.repository.DrugRepositoryInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugRepositoryImpl extends JpaRepository<DrugSchema, Long>, DrugRepositoryInterface {
    Optional<DrugSchema> findByBatchNumber(String batchNumber);
}