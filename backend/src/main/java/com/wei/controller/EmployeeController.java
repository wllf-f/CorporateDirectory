package com.wei.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.dto.QueryDto;
import com.wei.entity.Employee;
import com.wei.exception.BussinessException;
import com.wei.response.R;
import com.wei.response.ResponseCode;
import com.wei.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * knife4j 测试接口:http://localhost:8082/wei/doc.html#/default/%E5%91%98%E5%B7%A5%E4%BF%A1%E6%81%AF%E7%AE%A1%E7%90%86
 */
@RestController
@Tag(name = "员工信息管理")
@RequestMapping("/employee")
public class EmployeeController {

    @Operation(summary = "服务健康检查")
    @GetMapping("/health")
    public String healthCheck() {
        return "Service is UP!";
    }
    @Resource
    private EmployeeService employeeService;


    @Operation(summary = "添加员工")
    @PostMapping("/add")
    public R add(@RequestBody Employee employee){

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEmpNumber, employee.getEmpNumber());
        long count = employeeService.count(queryWrapper);
        if (count > 0) {
            throw new BussinessException(ResponseCode.USERNAME_EXIST);
        }

        employeeService.saveEmployee(employee);
        return R.success();
    }


    @Operation(summary = "查询所有员工（包含部门名称）")
    @PostMapping("/list")
    public R<Page<Map<String, Object>>> listWithDept(@RequestBody QueryDto dto) {


        Page<Map<String, Object>> page = employeeService.getEmployeesWithDepartmentPage(dto);
        return R.data(page);
    }


    @Operation(summary = "修改员工信息")
    @PutMapping("/update")
    public R update(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success();
    }

    @Operation(summary = "删除员工")
    @DeleteMapping("/delete")
    public R delete(@RequestParam int empId,@RequestParam String changeBy){
        employeeService.deleteEmployee(empId,changeBy);
        return R.success();
    }
    @Operation(summary = "彻底员工信息")
    @DeleteMapping("/remove")
    public R remove(@RequestParam int empId,@RequestParam String changeBy){
        employeeService.removeEmployeeById(empId,changeBy);
        return R.success();
    }
}