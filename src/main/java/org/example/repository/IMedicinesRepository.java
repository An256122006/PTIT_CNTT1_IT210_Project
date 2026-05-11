package org.example.repository;

import org.example.model.Medicines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMedicinesRepository extends JpaRepository<Medicines, Long> {
    @Query("SELECT m FROM Medicines m WHERE m.name LIKE %:search%")
    List<Medicines> findByNameContaining(String search);
}
