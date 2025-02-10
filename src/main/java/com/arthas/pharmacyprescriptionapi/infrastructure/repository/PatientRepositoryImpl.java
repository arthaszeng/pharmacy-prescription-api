package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.domain.repository.PatientRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepositoryImpl extends JpaRepository<PatientSchema, Long>, PatientRepositoryInterface {
    Optional<PatientSchema> findById(Long id);
}
