package com.example.medicalaisystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sex",nullable = false)
    private String sex;

    @Column(name = "age")
    private Integer age;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "id_card", nullable = false, unique = true)
    private String idCard;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "expertise",nullable = false)
    private String expertise;
}