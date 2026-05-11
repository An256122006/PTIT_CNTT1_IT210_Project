package org.example.service;

import org.example.dto.SpecialtiesDto;
import org.example.model.Specialties;

import java.util.List;

public interface ISpecialtiesService {
    List<Specialties> findAll();
    Specialties save(Specialties specialties);
    void delete(Long id);
    Specialties findById(Long id);
    Boolean existsByName(String name);
    Specialties update(Long id, SpecialtiesDto specialtiesDto);
}
