package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.entity.Employee;
import com.wei.dto.LoginRequestDto;
import com.wei.response.R;
import com.wei.response.ResponseCode;
import com.wei.service.EmployeeService;
import com.wei.config.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private EmployeeService employeeService;
    @Operation(summary = "员工登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginRequestDto loginRequest) {


        // 创建查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEmpNumber, loginRequest.getEmpNumber());
        queryWrapper.eq(Employee::getPassword, loginRequest.getPassword());
        queryWrapper.eq(Employee::getIsActive, "在职"); // 只允许状态为激活的用户登录

        Employee emp = employeeService.getOne(queryWrapper);
        boolean flag = false;
        String empNumber= null;
        Map<String, Object> innerGet = new HashMap<>();
        //登陆后验证，如果前端传回token，就跳过登陆验证
        if(emp!=null){
            flag = true;
            innerGet = employeeService.innerGetEmployeeById(emp.getEmpId());
            empNumber = (String) innerGet.get("empNumber");
        }else{
            if(jwtTokenUtil.validateToken(loginRequest.getToken(), loginRequest.getEmpNumber())){
                flag = true;
                innerGet = employeeService.innerGetEmployeeByNumber(loginRequest.getEmpNumber());
                empNumber = (String) innerGet.get("empNumber");
            }
        }


        if (flag) {
            // 登录成功，返回用户ID等必要信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("deptName",innerGet.get("deptName"));
            resultMap.put("isActive", innerGet.get("isActive"));
            resultMap.put("token",jwtTokenUtil.generateToken(empNumber));
            resultMap.put("empId", innerGet.get("empId"));
            resultMap.put("empNumber", innerGet.get("empNumber"));
            resultMap.put("empName", innerGet.get("empName"));
            resultMap.put("empRole", innerGet.get("empRole"));
            resultMap.put("empGender",innerGet.get("empGender"));
            resultMap.put("empPhone", innerGet.get("empPhone"));
            resultMap.put("empEmail", innerGet.get("empEmail"));
            resultMap.put("empCreateTime", innerGet.get("createTime"));
            resultMap.put("employeeCount",employeeService.getTotal().get("empTotal"));
            resultMap.put("deptCount",employeeService.getTotal().get("deptTotal"));
            resultMap.put("custCount",employeeService.getTotal().get("custTotal"));

            return R.data(resultMap);
        } else {
            // 登录失败，返回错误信息
            return R.fail(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
    }
}
