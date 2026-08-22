package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    // 根据手机号查询医生（你的前端可以用手机号/工号登录，这里用phone）
    Optional<Doctor> findByPhone(String phone);
    // ========== 新增：根据科室查询医生 ==========
    List<Doctor> findByDepartment(String department);
    // 新增：根据状态查询医生
    List<Doctor> findByStatus(String status);
}
