package com.example.medicalaisystem.controller;

import com.example.medicalaisystem.entity.HealthCarePlan;
import com.example.medicalaisystem.repository.HealthCarePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health/plan")
public class HealthCarePlanController {

    @Autowired
    private HealthCarePlanRepository healthCarePlanRepository;

    /**
     * 保存养生计划
     */
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody HealthCarePlan plan) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 生成计划编号
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String random = String.format("%03d", (int) ((Math.random() * 9 + 1) * 100));
            plan.setPlanNo("HCP" + date + random);

            // 设置默认状态
            if (plan.getStatus() == null) {
                plan.setStatus(1);
            }

            // 设置创建时间
            if (plan.getCreateTime() == null) {
                plan.setCreateTime(LocalDateTime.now());
            }

            healthCarePlanRepository.save(plan);
            map.put("code", 200);
            map.put("msg", "保存成功");
            map.put("data", plan);
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "保存失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * 查询所有计划
     */
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<HealthCarePlan> list = healthCarePlanRepository.findAll();
            map.put("code", 200);
            map.put("data", list);
            map.put("total", list.size());
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "查询失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * 根据用户ID查询计划
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getByUserId(@PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<HealthCarePlan> list = healthCarePlanRepository.findByUserIdOrderByCreateTimeDesc(userId);
            map.put("code", 200);
            map.put("data", list);
            map.put("total", list.size());
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "查询失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID查询单个计划
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            HealthCarePlan plan = healthCarePlanRepository.findById(id).orElse(null);
            if (plan == null) {
                map.put("code", 404);
                map.put("msg", "计划不存在");
                return map;
            }
            map.put("code", 200);
            map.put("data", plan);
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "查询失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * 更新计划状态
     */
    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        try {
            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());

            HealthCarePlan plan = healthCarePlanRepository.findById(id).orElse(null);
            if (plan == null) {
                map.put("code", 404);
                map.put("msg", "计划不存在");
                return map;
            }

            plan.setStatus(status);
            healthCarePlanRepository.save(plan);
            map.put("code", 200);
            map.put("msg", "更新成功");
            map.put("data", plan);
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "更新失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * 删除计划
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!healthCarePlanRepository.existsById(id)) {
                map.put("code", 404);
                map.put("msg", "计划不存在");
                return map;
            }
            healthCarePlanRepository.deleteById(id);
            map.put("code", 200);
            map.put("msg", "删除成功");
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "删除失败：" + e.getMessage());
        }
        return map;
    }
}