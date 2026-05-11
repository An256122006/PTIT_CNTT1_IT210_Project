package org.example.repository;

import org.example.model.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {
    @Query("select pd from PrescriptionDetail pd where pd.prescriptions.id = :prescriptionId")
    List<PrescriptionDetail> findByPrescriptionId(Long prescriptionId);

}
