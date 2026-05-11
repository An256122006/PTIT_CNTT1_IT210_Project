package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.SpecialtiesDto;
import org.example.model.Specialties;
import org.example.repository.ISpecialtiesRepository;
import org.example.service.ISpecialtiesService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SpecicaltiesService implements ISpecialtiesService {
    private final ISpecialtiesRepository specialtiesRepository;
    @Override
    public List<Specialties> findAll() {
        return specialtiesRepository.findAll();
    }

    @Override
    public Specialties save(Specialties specialties) {
        return specialtiesRepository.save(specialties);
    }

    @Override
    public void delete(Long id) {
        // Check if specialty is being used by any doctors
        if (!specialtiesRepository.existsById(id)) {
            throw new RuntimeException("Specialty not found");
        }
        // You might need to add a method to check if specialty is referenced by doctors
        // For now, let the database constraint handle it with a proper error message
        try {
            specialtiesRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete this specialty because it is being used by doctors");
        }
    }

    @Override
    public Specialties findById(Long id) {
        return specialtiesRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean existsByName(String name) {
        Specialties specialties = specialtiesRepository.findByName(name);
        return specialties != null;
    }

    public Specialties save(SpecialtiesDto specialtiesDto) {
        if (existsByName(specialtiesDto.getName())) {
            throw new RuntimeException("Specialty name already exists");
        }
        Specialties specialties = new Specialties();
        specialties.setName(specialtiesDto.getName());
        specialties.setDescription(specialtiesDto.getDescription());
        return save(specialties);
    }

    @Override
    public Specialties update(Long id, SpecialtiesDto specialtiesDto) {
        Specialties existingSpecialty = findById(id);
        if (existingSpecialty == null) {
            throw new RuntimeException("Specialty not found");
        }
        
        // Check if name already exists for another specialty
        Specialties specialtyWithSameName = specialtiesRepository.findByName(specialtiesDto.getName());
        if (specialtyWithSameName != null && !specialtyWithSameName.getId().equals(id)) {
            throw new RuntimeException("Specialty name already exists");
        }
        
        existingSpecialty.setName(specialtiesDto.getName());
        existingSpecialty.setDescription(specialtiesDto.getDescription());
        return save(existingSpecialty);
    }
}
