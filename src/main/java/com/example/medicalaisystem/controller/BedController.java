package com.example.medicalaisystem.controller;

import com.example.medicalaisystem.entity.Bed;
import com.example.medicalaisystem.repository.BedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bed")
public class BedController {

    private final BedRepository bedRepository;

    public BedController(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    // ====================== 1. 查询所有床位 ======================
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        Map<String, Object> res = new HashMap<>();
        List<Bed> list = bedRepository.findAll();
        res.put("code", 200);
        res.put("data", list);
        return ResponseEntity.ok(res);
    }

    // ====================== 2. 查询空闲床位 ======================
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> listAvailable() {
        Map<String, Object> res = new HashMap<>();
        List<Bed> list = bedRepository.findByStatus("空闲");
        res.put("code", 200);
        res.put("data", list);
        return ResponseEntity.ok(res);
    }

    // ====================== 3. 修改床位状态 + 病人姓名 ======================
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Integer id,
            @RequestBody Bed bedRequest
    ) {
        Map<String, Object> res = new HashMap<>();
        Optional<Bed> optional = bedRepository.findById(id);

        if (optional.isEmpty()) {
            res.put("code", 400);
            res.put("msg", "床位不存在");
            return ResponseEntity.badRequest().body(res);
        }

        Bed bed = optional.get();
        // 更新状态 + 病人姓名
        bed.setStatus(bedRequest.getStatus());
        bed.setPatientName(bedRequest.getPatientName());

        bedRepository.save(bed);

        res.put("code", 200);
        res.put("msg", "更新成功");
        return ResponseEntity.ok(res);
    }
}
