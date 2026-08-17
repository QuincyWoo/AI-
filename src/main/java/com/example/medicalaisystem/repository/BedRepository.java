package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BedRepository extends JpaRepository<Bed, Integer> {
}
