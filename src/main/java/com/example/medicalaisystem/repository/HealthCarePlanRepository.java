package com.example.medicalaisystem.repository;

import com.example.medicalaisystem.entity.HealthCarePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCarePlanRepository extends JpaRepository<HealthCarePlan, Long> {

    // 根据计划编号查询
    Optional<HealthCarePlan> findByPlanNo(String planNo);

    // 根据用户ID查询（按创建时间倒序）
    List<HealthCarePlan> findByUserIdOrderByCreateTimeDesc(Long userId);

    // 根据年龄段查询
    List<HealthCarePlan> findByAgeGroup(String ageGroup);

    // 查询有效计划
    List<HealthCarePlan> findByStatus(Integer status);

    // 删除计划
    void deleteByPlanNo(String planNo);
}