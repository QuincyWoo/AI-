package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, Integer> {
    SysUser findByPhone(String phone);
}