package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.PrescriptionDetail;
import org.example.model.Prescriptions;
import org.example.repository.IPrescriptionDetailRepository;
import org.example.service.IPrensionDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PrescriptionDetailService implements IPrensionDetailService {
    private final IPrescriptionDetailRepository prescriptionDetailRepository;
    @Override
    public void save(PrescriptionDetail prescriptionDetail) {
        prescriptionDetailRepository.save(prescriptionDetail);

    }

    @Override
    public List<PrescriptionDetail> findAll() {
        return prescriptionDetailRepository.findAll();
    }

    @Override
    public List<PrescriptionDetail> findByPrescriptionId(Long prescriptionId) {
        return prescriptionDetailRepository.findByPrescriptionId(prescriptionId);
    }

    @Override
    public void delete(Long id) {
        prescriptionDetailRepository.deleteById(id);
    }
}
