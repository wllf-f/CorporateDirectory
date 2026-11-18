package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.dto.QueryDto;
import com.wei.entity.Department;
import com.wei.exception.BussinessException;
import com.wei.response.R;
import com.wei.response.ResponseCode;
import com.wei.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "部门管理")
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

//    @Operation(summary = "查询所有部门（包含负责人姓名）")
//    @GetMapping("/list")
//    public R<List<Map<String, Object>>> list() {
//        List<Map<String, Object>> list = departmentService.getDepartmentsWithManager();
//        return R.data(list);
//    }

    @Operation(summary = "分页查询部门（包含负责人姓名）")
    @PostMapping("/list")
    public R<Page<Map<String, Object>>> page(
            @RequestBody QueryDto dto) {
        Page<Map<String, Object>> page = departmentService.getDepartmentsWithManagerPage(dto.getPageNumber(),dto.getPageSize());
        return R.data(page);
    }

    @Operation(summary = "添加部门")
    @PostMapping("/add")
    public R<String> add(@RequestBody Department department) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDeptName, department.getDeptName());
        long count = departmentService.count(queryWrapper);
        if (count > 0) {
            throw new BussinessException(ResponseCode.DEPTNAME_EXIST);
        }
        departmentService.save(department);
        return R.success();
    }
    @Operation(summary = "修改部门")
    @PutMapping("/update")
    public R<String> update(@RequestBody Department department) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDeptName, department.getDeptName());
        long count = departmentService.count(queryWrapper);
        if (count > 0) {
            throw new BussinessException(ResponseCode.DEPTNAME_EXIST);
        }
        departmentService.updateById(department);
        return R.success();
    }
    @Operation(summary = "删除部门")
    @DeleteMapping("/remove")
    public R<String> delete(@RequestParam Integer deptId,@RequestParam String changeBy) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDeptId, deptId);
        long count = departmentService.count(queryWrapper);
        if (count == 0) {
            throw new BussinessException(ResponseCode.DEPT_NOT_EXIST);
        }
        departmentService.removeDepartmentById(deptId,changeBy);
        return R.success();
    }

}