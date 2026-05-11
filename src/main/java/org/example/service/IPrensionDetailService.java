package org.example.service;

import org.example.model.PrescriptionDetail;
import org.example.model.Prescriptions;

import java.util.List;

public interface IPrensionDetailService {
    void save(PrescriptionDetail prescriptionDetail);
    List<PrescriptionDetail> findAll();
    List<PrescriptionDetail> findByPrescriptionId(Long prescriptionId);
    void delete(Long id);
}
