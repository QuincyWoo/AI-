package com.example.medicalaisystem.service.impl;

import com.example.medicalaisystem.entity.SysUser;
import com.example.medicalaisystem.repository.SysUserRepository;
import com.example.medicalaisystem.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser getUserInfo(Integer userId) {
        // 修复Optional问题：取Optional中的值，不存在返回null
        return sysUserRepository.findById(userId).orElse(null);
    }
}