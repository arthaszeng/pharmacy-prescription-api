package com.arthas.pharmacyprescriptionapi.infrastructure.repository;

import com.arthas.pharmacyprescriptionapi.domain.repository.PharmacyRepositoryInterface;
import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacySchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyRepositoryImpl extends JpaRepository<PharmacySchema, Long>, PharmacyRepositoryInterface {
    @Override
    Page<PharmacySchema> findAll(Pageable pageable);

    Optional<PharmacySchema> findById(Long id);
}