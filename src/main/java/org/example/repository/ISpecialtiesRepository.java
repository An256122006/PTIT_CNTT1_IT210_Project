package org.example.repository;

import org.example.model.Specialties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpecialtiesRepository extends JpaRepository<Specialties, Long> {
    Specialties findByName(String name);
    boolean existsById(Long id);
}
