package com.example.medicalaisystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer age;
    private String phone;
    private String idCard;
    private String gender;
    private String password;
}