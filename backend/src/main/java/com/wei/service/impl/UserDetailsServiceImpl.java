package com.wei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.entity.Employee;
import com.wei.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String empNumber) throws UsernameNotFoundException {
        // 1. 根据工号查询员工
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEmpNumber, empNumber);

        Employee employee = employeeMapper.selectOne(queryWrapper);

        if (employee == null) {
            throw new UsernameNotFoundException("用户不存在: " + empNumber);
        }

        // 2. 检查用户状态
        if (employee.getIsActive().equals("离职")) {
            throw new UsernameNotFoundException("用户已离职: " + empNumber);
        }

        // 3. 构建权限列表（根据员工角色设置权限）
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (employee.getEmpRole() != null && !employee.getEmpRole().isEmpty()) {
            // 添加角色权限，格式为 ROLE_xxx
            authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.getEmpRole().toUpperCase()));
        } else {
            // 默认角色
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }

        // 4. 返回 Spring Security 的 UserDetails 对象
        return new User(
                employee.getEmpNumber(),  // 用户名
                employee.getPassword(),    // 密码（数据库中的加密密码）
                authorities                // 权限列表
        );
    }
}