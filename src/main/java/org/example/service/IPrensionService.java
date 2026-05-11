package org.example.service;

import org.example.model.Prescriptions;

import java.time.LocalDate;
import java.util.List;

public interface IPrensionService {
    Prescriptions save(Prescriptions prescriptions);
    List<Prescriptions> findAll();
    List<Prescriptions> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String status);
    Prescriptions findById(Long id);
    void approvePrescription(Long id);
    void rejectPrescription(Long id);
    Prescriptions findByMedicalRecordId(Long medicalRecordId);
}
