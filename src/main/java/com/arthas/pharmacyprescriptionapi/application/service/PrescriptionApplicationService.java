package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PatientDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.PatientDomainService;
import com.arthas.pharmacyprescriptionapi.domain.service.PharmacyDomainService;
import com.arthas.pharmacyprescriptionapi.domain.service.PrescriptionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionApplicationService {
    private final PrescriptionDomainService prescriptionDomainService;
    private final PharmacyDomainService pharmacyDomainService;
    private final PatientDomainService patientDomainService;

    @Transactional
    public PrescriptionDomain createPrescription(PrescriptionDomain prescription) {
        PatientDomain patient = patientDomainService.getPatientById(prescription.getPatient().getId());

        PharmacyDomain pharmacy = pharmacyDomainService.getPharmacyById(prescription.getPharmacy().getId());

        List<PrescriptionDrugDomain> validatedDrugs = pharmacyDomainService.validateAndAllocateDrugs(
                pharmacy.getId(), prescription.getPrescriptionDrugs()
        );

        PrescriptionDomain completePrescription = PrescriptionDomain.builder()
                .patient(patient)
                .pharmacy(pharmacy)
                .prescriptionDrugs(validatedDrugs)
                .status("PENDING")
                .build();

        return prescriptionDomainService.createPrescription(completePrescription);
    }
}