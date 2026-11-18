package com.wei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.dto.QueryDto;
import com.wei.entity.Employee;

import java.util.Map;


public interface EmployeeService extends IService<Employee> {
    Page<Map<String, Object>> getEmployeesWithDepartmentPage(QueryDto dto);
    void deleteEmployee(Integer empId, String empNumber);

    Map<String, Object> innerGetEmployeeById(Integer empId);

    Map<String, Object> innerGetEmployeeByNumber(String empNumber);

    Map<String, Integer> getTotal();

    boolean removeEmployeeById(Integer empId, String empNumber);

    boolean saveEmployee(Employee entity);

}
