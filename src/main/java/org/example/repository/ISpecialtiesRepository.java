package org.example.repository;

import org.example.model.Specialties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISpecialtiesRepository extends JpaRepository<Specialties, Long> {
    Specialties findByName(String name);
    List<Specialties> findByNameContainingIgnoreCase(String name);
    boolean existsById(Long id);
}
