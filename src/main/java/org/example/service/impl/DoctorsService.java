package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.Doctors;
import org.example.repository.IDoctorRepository;
import org.example.service.IDoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DoctorsService implements IDoctorService {
    private final IDoctorRepository doctorRepository;
    @Override
    public List<Doctors> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctors save(Doctors doctors) {
        return null;
    }

    @Override
    public Doctors findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Doctors> findBySpecialtiesId(Long specialtiesId) {
        return doctorRepository.findBySpecialtiesId(specialtiesId);
    }

    @Override
    public Doctors findByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }
}
