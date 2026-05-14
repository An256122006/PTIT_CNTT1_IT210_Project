package org.example.service.impl;

import org.example.model.Medicines;
import org.example.model.Payments;
import org.example.repository.IMedicinesRepository;
import org.example.service.IPaymentService;
import org.example.service.IUserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.PrescriptionDetail;
import org.example.model.Prescriptions;
import org.example.repository.IPrescriptionDetailRepository;
import org.example.repository.IPrescriptionsRepository;
import org.example.service.IPrensionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PrescriptionService implements IPrensionService {
    private final IPrescriptionsRepository prescriptionsRepository;
    private final IPrescriptionDetailRepository prescriptionDetailRepository;
    private final IMedicinesRepository medicinesRepository;
    private final IPaymentService paymentService;
    private final IUserService userService;

    @Override
    public Prescriptions save(Prescriptions prescriptions) {
        return prescriptionsRepository.save(prescriptions);
    }

    @Override
    public List<Prescriptions> findAll() {
        return prescriptionsRepository.findAll();
    }

    @Override
    public List<Prescriptions> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String status) {
        return prescriptionsRepository.findByDateRangeAndStatus(startDate, endDate, status);
    }

    @Override
    public Prescriptions findById(Long id) {
        return prescriptionsRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePrescription(Long id) {
        Prescriptions prescriptions = prescriptionsRepository.findById(id).orElse(null);
        if (prescriptions == null) {
            throw new RuntimeException("Đơn thuốc không tồn tại");
        }
        List<PrescriptionDetail> details = prescriptionDetailRepository.findByPrescriptionId(id);
        double totalAmount = 0;
        for (PrescriptionDetail detail : details) {
            Medicines medicines = medicinesRepository.findById(detail.getMedicines().getId()).orElse(null);
            if (detail.getQuantity() > medicines.getQuantity()) {
                throw new RuntimeException("Số lượng thuốc không đủ: " + medicines.getName() + 
                    " (Cần: " + detail.getQuantity() + ", Tồn kho: " + medicines.getQuantity() + ")");
            }
            totalAmount+=detail.getMedicines().getPrice() * detail.getQuantity();
            medicines.setQuantity(medicines.getQuantity() - detail.getQuantity());
            medicinesRepository.save(medicines);
        }
        Payments payments = new Payments();
        payments.setAmount( totalAmount);
        payments.setAdmin(userService.findById(1L));
        payments.setPrescription(prescriptions);
        payments.setStatus("PENDING");
        payments.setPaymentDate(LocalDate.now());
        paymentService.save(payments);
        prescriptions.setStatus("APPROVED");
        prescriptionsRepository.save(prescriptions);
    }

    @Override
    public void rejectPrescription(Long id) {
        Prescriptions prescriptions = prescriptionsRepository.findById(id).orElse(null);
        if (prescriptions == null) {
            throw new RuntimeException("Đơn thuốc không tồn tại");
        }
        prescriptions.setStatus("REJECTED");
        prescriptionsRepository.save(prescriptions);
    }

    @Override
    public Prescriptions findByMedicalRecordId(Long medicalRecordId) {
        return prescriptionsRepository.findByMedicalRecordId(medicalRecordId);
    }

}


