package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.dto.QueryDto;
import com.wei.entity.Employee;
import com.wei.entity.OperLog;
import com.wei.mapper.EmployeeMapper;
import com.wei.mapper.OperLogMapper;
import com.wei.service.EmployeeService;
import com.wei.service.OperLogService;
import com.wei.tools.SelfTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>  implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private OperLogService operLogService;

    @Override
    public Page<Map<String, Object>> getEmployeesWithDepartmentPage(QueryDto dto) {
        int offset = (dto.getPageNumber() - 1) * dto.getPageSize();

        // 获取所有记录
        List<Map<String, Object>> records = employeeMapper.selectEmployeesWithDepartment();

        List<Map<String, Object>> newRecords = new ArrayList<>();

        // 搜索
        SelfTools tools = new SelfTools();
        newRecords = tools.searchCustomer(records,dto.getData());

        // 手动分页
        List<Map<String, Object>> pageList = newRecords.stream()
                .skip(offset)
                .limit(dto.getPageSize())
                .collect(Collectors.toList());

        int total = newRecords.size();

        Page<Map<String, Object>> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        page.setRecords(pageList);
        page.setTotal(total);
        return page;
    }
    public Map<String, Object> innerGetEmployeeById(Integer empId){
        return employeeMapper.innerGetEmployeeById(empId);
    }
    public Map<String, Object> innerGetEmployeeByNumber(String number) {
        return employeeMapper.innerGetEmployeeByNumber(number);
    }

    @Override
    public void deleteEmployee(Integer empId, String changeBy) {
        OperLog operLog = new OperLog();
        operLog.setOperatorId(changeBy);
        operLog.setOperation("删除员工信息");
        operLog.setOperatedTable("employee");
        operLog.setTableId(empId);
        operLog.setOldData(employeeMapper.innerGetEmployeeById(empId).toString());
        operLog.setNewData("无");
        operLogService.addOperLog(operLog);
        employeeMapper.deleteEmployee(empId);
    }
    @Override
    public Map<String, Integer> getTotal(){
        return employeeMapper.getTotal();
    }

    public boolean removeEmployeeById(Integer empId, String empNumber){
        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("彻底删除员工信息");
        operLog.setOperatedTable("employee");
        operLog.setTableId(empId);
        operLog.setOldData(employeeMapper.innerGetEmployeeById(empId).toString());
        operLog.setNewData("无");
        operLogService.addOperLog(operLog);
        return employeeMapper.deleteEmployeeById(empId)>=1;
    }

    // 保存员工信息


    public boolean saveEmployee(Employee entity) {
         int flag = employeeMapper.saveEmployee(entity);
         OperLog operLog = new OperLog();
         operLog.setOperatorId(entity.getChangeBy());
         operLog.setOperation("添加员工信息");
         operLog.setOperatedTable("employee");
         long tableId = (long) innerGetEmployeeByNumber(entity.getEmpNumber()).get("empId");
         operLog.setTableId((int) tableId);
         operLog.setOldData("无");
         operLog.setNewData(entity.toString());
         operLogService.addOperLog(operLog);
         return flag>=1;
    }

    @Override
    public boolean updateById(Employee entity) {
        Map<String, Object> oldData = employeeMapper.innerGetEmployeeById(entity.getEmpId());
        OperLog operLog = new OperLog();
        operLog.setOperatorId(entity.getChangeBy());
        operLog.setOperation("修改员工信息");
        operLog.setOperatedTable("employee");
        operLog.setTableId(entity.getEmpId());
        operLog.setOldData(oldData.toString());
        operLog.setNewData(entity.toString());
        operLogService.addOperLog(operLog);
        return super.updateById(entity);
    }
}
