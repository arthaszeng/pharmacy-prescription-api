package com.arthas.pharmacyprescriptionapi.application.service;

import com.arthas.pharmacyprescriptionapi.domain.model.PatientDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PharmacyDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDomain;
import com.arthas.pharmacyprescriptionapi.domain.model.PrescriptionDrugDomain;
import com.arthas.pharmacyprescriptionapi.domain.service.AuditLogDomainService;
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
    private final AuditLogDomainService auditLogDomainService;

    @Transactional
    public PrescriptionDomain createPrescription(PrescriptionDomain prescription) {
        PatientDomain patient = patientDomainService.getPatientById(prescription.getPatient().getId());

        PharmacyDomain pharmacy = pharmacyDomainService.getPharmacyById(prescription.getPharmacy().getId());

        List<PrescriptionDrugDomain> validatedDrugs = pharmacyDomainService.validateAndAllocateDrugs(
                pharmacy, prescription.getPrescriptionDrugs()
        );

        PrescriptionDomain completePrescription = PrescriptionDomain.builder()
                .patient(patient)
                .pharmacy(pharmacy)
                .prescriptionDrugs(validatedDrugs)
                .status("PENDING")
                .build();

        return prescriptionDomainService.createPrescription(completePrescription);
    }

    @Transactional
    public PrescriptionDomain fulfillPrescription(Long prescriptionId) {
        PrescriptionDomain prescription = prescriptionDomainService.getPrescriptionById(prescriptionId);

        if (!"PENDING".equals(prescription.getStatus())) {
            throw new IllegalArgumentException("Prescription ID " + prescriptionId + " is not pending fulfillment");
        }

        try {
            PharmacyDomain pharmacy = pharmacyDomainService.getPharmacyById(prescription.getPharmacy().getId());
            pharmacyDomainService.validateAndDeductStock(pharmacy, prescription.getPrescriptionDrugs());

            PrescriptionDomain fulfilledPrescription = prescriptionDomainService.fulfillPrescription(prescription);

            auditLogDomainService.logFulfillSuccess(fulfilledPrescription);

            return fulfilledPrescription;
        } catch (Exception e) {
            auditLogDomainService.logFulfillFailure(prescription, e.getMessage());

            throw e;
        }
    }
}