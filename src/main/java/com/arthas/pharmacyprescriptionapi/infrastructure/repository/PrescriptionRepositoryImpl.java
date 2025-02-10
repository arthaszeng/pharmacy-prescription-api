package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.domain.repository.PrescriptionRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepositoryImpl extends JpaRepository<PrescriptionSchema, Long>, PrescriptionRepositoryInterface {
    Optional<PrescriptionSchema> findById(Long id);
}