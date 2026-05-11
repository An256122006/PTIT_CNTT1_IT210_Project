package org.example.service;

import org.example.dto.MedicineDto;
import org.example.model.Medicines;

import java.util.List;

public interface IMedicineService {
    List<Medicines> findAll(String search);
    void addMedicine(MedicineDto medicineDto);
    void updateMedicine(Long id, MedicineDto medicineDto);
    void deleteMedicine(Long id);
    Medicines findMedicineById(Long id);
}
