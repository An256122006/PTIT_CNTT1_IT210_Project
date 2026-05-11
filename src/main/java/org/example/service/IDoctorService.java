package org.example.service;

import org.example.model.Doctors;

import java.util.List;

public interface IDoctorService {
    List<Doctors> findAll();
    Doctors save(Doctors doctors);
    Doctors findById(Long id);
    List<Doctors> findBySpecialtiesId(Long specialtiesId);
    Doctors findByUserId(Long userId);
}
