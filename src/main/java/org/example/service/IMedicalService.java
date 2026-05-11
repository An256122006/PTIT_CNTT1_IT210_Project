package org.example.service;

import org.example.model.MedicalRecords;

import java.time.LocalDate;
import java.util.List;

public interface IMedicalService {
    void save(MedicalRecords medicalRecords);
    List<MedicalRecords> findAll();
    List<MedicalRecords> findByPatientId(Long patientId);
    List<MedicalRecords> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    MedicalRecords findById(Long id);
    MedicalRecords findByAppointmentsId(Long appointmentId);
}
