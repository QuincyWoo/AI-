package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Optional<Appointment> findByPhoneAndDoctorNameAndAppointTime(String phone, String doctorName, String appointTime);
    List<Appointment> findByDoctorName(String doctorName);
    List<Appointment> findByDoctorNameAndStatus(String doctorName, String status);
}