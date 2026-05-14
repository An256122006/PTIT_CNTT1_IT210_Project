package org.example.repository;

import org.example.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payments, Long> {
    @Query("select p from Payments p where p.prescription.id = :prescriptionId")
    List<Payments> findByPrescriptionId(Long prescriptionId);
    @Query("select p from Payments p where p.prescription.medicalRecord.appointments.patient.id=:patientId")
    List<Payments> findByPatientId(Long patientId);
    @Query("select p from Payments p where p.status = :status")
    List<Payments> findByStatus(String status);
}
