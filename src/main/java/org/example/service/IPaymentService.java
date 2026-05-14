package org.example.service;

import org.example.model.Payments;

import java.util.List;

public interface IPaymentService {
    List<Payments> findByPrescriptionId(Long prescriptionId);
    void save(Payments payments);
    List<Payments> findByPatientId(Long patientId);
    Payments findById(Long id);
    List<Payments> findByStatus(String status);
}
