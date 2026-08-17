package com.example.medicalaisystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bed")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String floor;       // 楼层
    private String roomNo;      // 房号
    private String bedNo;       // 床位号
    private String status;      // 状态：空闲/已占用/维修中
    private String patientName; // 病人姓名

    private LocalDateTime createTime;
}