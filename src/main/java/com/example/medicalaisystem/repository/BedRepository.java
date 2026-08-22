package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Integer> {
    List<Bed> findByStatus(String status);
}
