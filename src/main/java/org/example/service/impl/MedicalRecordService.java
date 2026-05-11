package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.MedicalRecords;
import org.example.repository.IMedicalRecordsRepository;
import org.example.service.IMedicalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MedicalRecordService implements IMedicalService {
    private final IMedicalRecordsRepository medicalRecordsRepository;
    @Override
    public void save(MedicalRecords medicalRecords) {
        medicalRecordsRepository.save(medicalRecords);
    }
    @Override
    public List<MedicalRecords> findAll() {
        return medicalRecordsRepository.findAll();
    }
    
    @Override
    public List<MedicalRecords> findByPatientId(Long patientId) {
        return medicalRecordsRepository.findByPatientId(patientId);
    }

    @Override
    public List<MedicalRecords> findByDoctorIdAndDate(Long doctorId, LocalDate date) {
        return medicalRecordsRepository.findByDoctorIdAndDate(doctorId, date);
    }

    @Override
    public MedicalRecords findById(Long id) {
        return medicalRecordsRepository.findById(id).orElse(null);
    }

    @Override
    public MedicalRecords findByAppointmentsId(Long appointmentId) {
        return medicalRecordsRepository.findByAppointmentsId(appointmentId);
    }
}
