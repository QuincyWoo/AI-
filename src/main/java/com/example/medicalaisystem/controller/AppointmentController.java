package com.example.medicalaisystem.controller;

import com.example.medicalaisystem.entity.Appointment;
import com.example.medicalaisystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appoint")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody Appointment app) {
        Map<String, Object> map = new HashMap<>();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        app.setAppointNo("YY" + date + random);
        app.setCreateTime(new Date());
        app.setStatus("待就诊");
        appointmentRepository.save(app);
        map.put("code", 200);
        map.put("msg", "挂号成功");
        return map;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", appointmentRepository.findAll());
        return map;
    }
    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        Integer id = (Integer) params.get("id");
        String status = (String) params.get("status");
        Appointment data = appointmentRepository.findById(id).orElse(null);
        if (data != null) {
            data.setStatus(status);
            appointmentRepository.save(data);
            map.put("code", 200);
            map.put("msg", "更新成功");
        }
        return map;
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String phone = params.get("phone");
        String doctorName = params.get("doctorName");
        String appointTime = params.get("appointTime");
        Optional<Appointment> opt = appointmentRepository.findByPhoneAndDoctorNameAndAppointTime(phone, doctorName, appointTime);
        if (opt.isPresent()) {
            Appointment app = opt.get();
            app.setStatus("已取消");
            appointmentRepository.save(app);
            map.put("code", 200);
            map.put("msg", "取消成功");
        } else {
            map.put("code", 404);
            map.put("msg", "未找到该预约记录");
        }
        return map;
    }
}