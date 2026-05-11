package org.example.repository;

import org.example.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentsRepository extends JpaRepository<Appointments, Long> {
    @Query("""
    SELECT COUNT(a) > 0
    FROM Appointments a
    WHERE a.doctor.id = :doctorId
    AND :startDate < a.endDate
    AND :endDate > a.startDate
    AND (
        a.status = 'CONFIRMED'
        OR a.status = 'PENDING'
    )
""")
    Boolean checkOverlap(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    @Query("""
    select a from Appointments a
    where a.startDate <= :endDate
    and a.endDate >= :startDate
""")
    List<Appointments> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    List<Appointments> findByPatientId(Long patientId);
    @Query("select a from Appointments a where a.doctor.id = :doctorId and a.status = 'PENDING'")
    List<Appointments> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId);
    @Query("select a from Appointments a where a.doctor.id = :doctorId")
    List<Appointments> findByDoctorId(@Param("doctorId") Long doctorId);
}
