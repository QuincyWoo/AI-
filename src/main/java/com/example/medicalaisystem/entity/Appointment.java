package com.example.medicalaisystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String appointNo;
    private String userName;
    private String phone;
    private String deptName;
    private String clinicName;
    private String doctorName;
    private String appointTime;
    private String description;
    private String status; // 状态：待就诊、就诊中、已完成
    private String paymentItems;
    private Integer totalAmount;
    private Date createTime;
}