package com.example.medicalaisystem.controller;

import com.example.medicalaisystem.entity.SysUser;
import com.example.medicalaisystem.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SysUserRepository sysUserRepository;

    // 手机号 + 密码 登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody SysUser user) {
        Map<String, Object> map = new HashMap<>();
        String phone = user.getPhone();
        String password = user.getPassword();

        SysUser sysUser = sysUserRepository.findByPhone(phone);

        if (sysUser == null) {
            map.put("code", 500);
            map.put("msg", "该手机号未注册");
            return map;
        }
        if (!sysUser.getPassword().equals(password)) {
            map.put("code", 500);
            map.put("msg", "密码错误");
            return map;
        }

        map.put("code", 200);
        map.put("msg", "登录成功");
        map.put("userId", sysUser.getId());
        map.put("name", sysUser.getName());
        return map;
    }

    /**
     * 用户注册接口
     * 接收字段：name(姓名), phone(手机号), idCard(身份证号), gender(性别), password(密码)
     * id 和 createTime 自动生成
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody SysUser user) {
        Map<String, Object> map = new HashMap<>();

        // 1. 参数校验
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            map.put("code", 400);
            map.put("msg", "姓名不能为空");
            return map;
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            map.put("code", 400);
            map.put("msg", "手机号不能为空");
            return map;
        }
        if (user.getIdCard() == null || user.getIdCard().trim().isEmpty()) {
            map.put("code", 400);
            map.put("msg", "身份证号不能为空");
            return map;
        }
        if (user.getGender() == null || user.getGender().trim().isEmpty()) {
            map.put("code", 400);
            map.put("msg", "性别不能为空");
            return map;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            map.put("code", 400);
            map.put("msg", "密码不能为空");
            return map;
        }
        if (user.getAge() == null || user.getAge() < 0 || user.getAge() > 150) {
            map.put("code", 400);
            map.put("msg", "年龄不合法（需为0-150之间的整数）");
            return map;
        }

        // 2. 手机号格式校验（简单校验）
        String phone = user.getPhone().trim();
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            map.put("code", 400);
            map.put("msg", "手机号格式不正确");
            return map;
        }

        // 3. 身份证号格式校验（简单校验）
        String idCard = user.getIdCard().trim();
        if (!idCard.matches("^\\d{17}[\\dXx]$")) {
            map.put("code", 400);
            map.put("msg", "身份证号格式不正确（需为18位）");
            return map;
        }

        // 4. 检查手机号是否已被注册
        SysUser exist = sysUserRepository.findByPhone(phone);
        if (exist != null) {
            map.put("code", 409);
            map.put("msg", "该手机号已被注册");
            return map;
        }


        try {
            // 6. 设置自动生成的字段
            user.setPhone(phone);
            user.setIdCard(idCard);
            user.setName(user.getName().trim());
            user.setGender(user.getGender().trim());
            user.setPassword(user.getPassword().trim());
            // id 由数据库自动生成（自增主键）
            // createTime 由数据库自动生成（默认当前时间）

            // 7. 保存用户
            SysUser savedUser = sysUserRepository.save(user);

            map.put("code", 200);
            map.put("msg", "注册成功");
            map.put("userId", savedUser.getId());
            map.put("name", savedUser.getName());
            map.put("phone", savedUser.getPhone());

        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "注册失败，服务器内部错误：" + e.getMessage());
            e.printStackTrace();
        }

        return map;
    }

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SysUser user = sysUserRepository.findById(userId).orElse(null);
            if (user == null) {
                result.put("code", 500);
                result.put("msg", "用户不存在");
                return result;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("name", user.getName());
            data.put("phone", user.getPhone());
            data.put("idCard", user.getIdCard());
            data.put("gender", user.getGender());
            data.put("age", user.getAge());

            result.put("code", 200);
            result.put("msg", "请求成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取信息失败");
        }
        return result;
    }
}