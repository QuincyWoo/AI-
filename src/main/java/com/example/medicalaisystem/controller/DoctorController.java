package com.example.medicalaisystem.controller;

import com.example.medicalaisystem.dto.DoctorLoginRequest;
import com.example.medicalaisystem.entity.Doctor;
import com.example.medicalaisystem.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody DoctorLoginRequest request) {
        Map<String, Object> result = new HashMap<>();

        Optional<Doctor> doctorOpt = doctorRepository.findByPhone(request.getUsername());
        if (doctorOpt.isEmpty()) {
            result.put("code", 400);
            result.put("msg", "手机号不存在");
            return ResponseEntity.badRequest().body(result);
        }

        Doctor doctor = doctorOpt.get();
        if (!doctor.getPassword().equals(request.getPassword())) {
            result.put("code", 400);
            result.put("msg", "密码错误");
            return ResponseEntity.badRequest().body(result);
        }

        result.put("code", 200);
        result.put("msg", "登录成功");

        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctor.getId());
        data.put("doctorName", doctor.getName());
        data.put("department", doctor.getDepartment());
        data.put("age", doctor.getAge());
        data.put("id_card", doctor.getIdCard());
        data.put("sex",doctor.getSex());
        data.put("phone", doctor.getPhone());

        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updatePwd")
    public ResponseEntity<Map<String, Object>> updatePwd(@RequestBody Map<String, String> params) {
        Map<String, Object> res = new HashMap<>();
        String phone = params.get("phone");
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");

        Optional<Doctor> doctorOpt = doctorRepository.findByPhone(phone);
        if (doctorOpt.isEmpty()) {
            res.put("code", 400);
            res.put("msg", "账号不存在");
            return ResponseEntity.badRequest().body(res);
        }
        Doctor doctor = doctorOpt.get();
        if (!doctor.getPassword().equals(oldPwd)) {
            res.put("code", 400);
            res.put("msg", "原密码错误");
            return ResponseEntity.badRequest().body(res);
        }
        doctor.setPassword(newPwd);
        doctorRepository.save(doctor);
        res.put("code", 200);
        res.put("msg", "密码修改成功");
        return ResponseEntity.ok(res);
    }

    // ========== 新增：根据科室获取医生列表 ==========
    @GetMapping("/by-department")
    public ResponseEntity<Map<String, Object>> getDoctorsByDepartment(
            @RequestParam("department") String department
    ) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 根据科室查询医生
            List<Doctor> doctors = doctorRepository.findByDepartment(department);

            if (doctors == null || doctors.isEmpty()) {
                result.put("code", 200);
                result.put("msg", "该科室暂无医生");
                result.put("data", new ArrayList<>());
                return ResponseEntity.ok(result);
            }

            // 构建返回数据
            List<Map<String, Object>> doctorList = new ArrayList<>();
            for (Doctor doctor : doctors) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("id", doctor.getId());
                doc.put("name", doctor.getName());
                doc.put("age", doctor.getAge());
                doc.put("sex",doctor.getSex());
                doc.put("department", doctor.getDepartment());
                doc.put("phone", doctor.getPhone());
                // 由于实体类没有这些字段，使用默认值
                doc.put("title", "主治医师");           // 默认职称
                doc.put("expertise", "全科诊疗");        // 默认擅长
                doc.put("avatar", "/default-avatar.png"); // 默认头像
                doc.put("rating", 4.5);                  // 默认评分
                doctorList.add(doc);
            }

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", doctorList);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", new ArrayList<>());
            return ResponseEntity.status(500).body(result);
        }
    }
}