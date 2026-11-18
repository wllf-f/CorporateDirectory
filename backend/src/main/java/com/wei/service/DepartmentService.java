package com.wei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.entity.Department;
import java.util.List;
import java.util.Map;

public interface DepartmentService extends IService<Department> {

    /**
     * 查询部门列表（包含负责人姓名）
     */
    List<Map<String, Object>> getDepartmentsWithManager();

    /**
     * 分页查询部门列表（包含负责人姓名）
     */
    Page<Map<String, Object>> getDepartmentsWithManagerPage(Integer pageNum, Integer pageSize);

    boolean removeDepartmentById(Integer deptId, String empNumber);
}