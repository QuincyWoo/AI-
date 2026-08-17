package com.example.medicalaisystem.dto;

import lombok.Data;

@Data
public class DoctorLoginRequest {
    private String username; // 对应前端的“工号/手机号”
    private String password;
}