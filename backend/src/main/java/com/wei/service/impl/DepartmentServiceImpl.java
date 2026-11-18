package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.entity.Department;
import com.wei.entity.Employee;
import com.wei.entity.OperLog;
import com.wei.mapper.DepartmentMapper;
import com.wei.service.DepartmentService;
import com.wei.service.OperLogService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private OperLogService operLogService;

    @Override
    public List<Map<String, Object>> getDepartmentsWithManager() {
        return departmentMapper.selectDepartmentsWithManager();
    }

    @Override
    public Page<Map<String, Object>> getDepartmentsWithManagerPage(Integer pageNum, Integer pageSize) {


        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 查询数据
        List<Map<String, Object>> records = departmentMapper.selectDepartmentsWithManagerPage(offset, pageSize);



        // 查询总数
        Long total = departmentMapper.countDepartments();

        // 创建分页对象
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        page.setRecords(records);
        page.setTotal(total);

        return page;
    }

    @Override
    public boolean save(Department entity) {
        boolean flag = super.save(entity);
        OperLog operLog = new OperLog();
        operLog.setOperatorId(entity.getChangeBy());
        operLog.setOperation("添加部门");
        operLog.setOperatedTable("department");
        operLog.setTableId(entity.getDeptId());
        operLog.setOldData("无");
        operLog.setNewData(entity.toString());
        operLogService.addOperLog(operLog);
        return flag;
    }

    @Override
    public boolean updateById(Department entity) {
        boolean flag = super.updateById(entity);
        OperLog operLog = new OperLog();
        operLog.setOperatorId(entity.getChangeBy());
        operLog.setOperation("修改部门信息");
        operLog.setOperatedTable("department");
        operLog.setTableId(entity.getDeptId());
        operLog.setOldData(departmentMapper.selectById(entity.getDeptId()).toString());
        operLog.setNewData(entity.toString());
        operLogService.addOperLog(operLog);
        return flag;
    }

    public boolean removeDepartmentById(Integer deptId, String empNumber){
        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("彻底删除部门");
        operLog.setOperatedTable("department");
        //能否取到ID
        operLog.setTableId(deptId);
        operLog.setOldData(departmentMapper.selectById(deptId).toString());
        operLog.setNewData("无");
        operLogService.addOperLog(operLog);
        return departmentMapper.deleteById(deptId)>=1;
    }
}