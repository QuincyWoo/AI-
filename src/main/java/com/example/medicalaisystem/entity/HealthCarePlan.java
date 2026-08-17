package com.example.medicalaisystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_care_plan")
@Data
public class HealthCarePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_no", unique = true, nullable = false, length = 32)
    private String planNo;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "age_group", nullable = false, length = 20)
    private String ageGroup;

    @Column(name = "user_info", length = 500)
    private String userInfo;

    @Column(name = "plan_content", nullable = false, columnDefinition = "JSON")
    private String planContent;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}