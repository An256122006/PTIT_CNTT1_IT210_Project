package org.example.repository;

import org.example.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDoctorRepository extends JpaRepository<Doctors, Long> {
    @Query("SELECT d FROM Doctors d WHERE d.specialties.id=:specialtiesId")
    List<Doctors> findBySpecialtiesId(@Param("specialtiesId") Long specialtiesId);
    @Query("SELECT d FROM Doctors d WHERE d.users.id=:userId")
    Doctors findByUserId(@Param("userId") Long userId);
}
