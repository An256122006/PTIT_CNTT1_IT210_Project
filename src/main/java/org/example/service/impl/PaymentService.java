package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.Payments;
import org.example.repository.IPaymentRepository;
import org.example.service.IPaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final IPaymentRepository paymentRepository;
    @Override
    public List<Payments> findByPrescriptionId(Long prescriptionId) {
        return paymentRepository.findByPrescriptionId(prescriptionId);
    }

    @Override
    public void save(Payments payments) {
        paymentRepository.save(payments);
    }
    @Override
    public List<Payments> findByPatientId(Long patientId) {
        return paymentRepository.findByPatientId(patientId);
    }
    @Override
    public Payments findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
    @Override
    public List<Payments> findByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }
}
