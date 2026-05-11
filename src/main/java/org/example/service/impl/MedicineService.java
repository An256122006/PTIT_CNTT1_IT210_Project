package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.MedicineDto;
import org.example.model.Medicines;
import org.example.model.Status;
import org.example.repository.IMedicinesRepository;
import org.example.service.IMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService implements IMedicineService {
    private final IMedicinesRepository medicineRepository;
    @Override
    public List<Medicines> findAll(String search) {
        return medicineRepository.findByNameContaining(search);
    }
    @Override
    public void addMedicine(MedicineDto medicineDto) {
        Medicines medicine = new Medicines();
        medicine.setName(medicineDto.getName());
        medicine.setDescription(medicineDto.getDescription());
        medicine.setPrice(medicineDto.getPrice());
        medicine.setQuantity(medicineDto.getQuantity());
        medicine.setStatus(Status.ACTIVE);
        medicineRepository.save(medicine);

    }

    @Override
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new RuntimeException("Thuốc không tồn tại");
        }
        medicineRepository.deleteById(id);
    }

    @Override
    public void updateMedicine(Long id, MedicineDto medicineDto) {
        Medicines medicine = medicineRepository.findById(id).orElse(null);
        if (medicine == null) {
            throw new RuntimeException("Thuốc không tồn tại");
        }
        medicine.setName(medicineDto.getName());
        medicine.setDescription(medicineDto.getDescription());
        medicine.setPrice(medicineDto.getPrice());
        medicine.setQuantity(medicineDto.getQuantity());
        medicineRepository.save(medicine);
    }

    @Override
    public Medicines findMedicineById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }
}
