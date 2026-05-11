package org.example.repository;

import org.example.model.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IPrescriptionsRepository extends JpaRepository<Prescriptions, Long> {
    @Query("""
            select p from Prescriptions p
            where
            (:startDate is null or p.medicalRecord.date >= :startDate)
            and
            (:endDate is null or p.medicalRecord.date <= :endDate)
            and
            (:status is null or p.status = :status)
            """)
    List<Prescriptions> findByDateRangeAndStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status
    );

    @Query("SELECT p FROM Prescriptions p WHERE p.medicalRecord.id = :medicalRecordId")
    Prescriptions findByMedicalRecordId(@Param("medicalRecordId") Long medicalRecordId);
}
