package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.DrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.DrugDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrugApplicationService {
    private final DrugDomainService drugDomainService;

    @Transactional
    public DrugDomain addDrug(DrugDomain drug) {
        return drugDomainService.addDrug(drug);
    }

    @Transactional(readOnly = true)
    public Page<DrugDomain> getAllDrugs(Pageable pageable) {
        return drugDomainService.getAllDrugs(pageable);
    }
}