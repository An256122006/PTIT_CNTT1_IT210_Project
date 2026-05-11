package org.example.repository;

import org.example.model.MedicalRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IMedicalRecordsRepository extends JpaRepository<MedicalRecords, Long> {


    @Query("SELECT mr FROM MedicalRecords mr JOIN mr.appointments a WHERE a.patient.id = :patientId")
    List<MedicalRecords> findByPatientId(Long patientId);

    @Query("SELECT mr FROM MedicalRecords mr WHERE mr.appointments.id = :appointmentId")
    MedicalRecords findByAppointmentsId(Long appointmentId);

    @Query("""
            SELECT mr
            FROM MedicalRecords mr
            WHERE mr.appointments.doctor.id = :doctorId
            AND mr.date = :date AND mr.appointments.status = 'CONFIRMED'
            """)
    List<MedicalRecords> findByDoctorIdAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date
    );
}
