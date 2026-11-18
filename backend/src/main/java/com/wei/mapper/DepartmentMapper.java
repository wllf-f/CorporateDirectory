package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    @Select("SELECT " +
            "   d.dept_id AS deptId, " +
            "   d.dept_name AS deptName, " +
            "   e.emp_name AS managerName, " +
            "   e.emp_phone AS managerTel, " +
            "   d.create_time AS createTime, " +
            "   d.update_time AS updateTime " +
            "FROM department d " +
            "LEFT JOIN employee e ON d.manager_id = e.emp_id " +
            "ORDER BY d.dept_id")
    List<Map<String, Object>> selectDepartmentsWithManager();

    /**
     * 分页查询部门列表，包含负责人姓名
     */
    @Select("SELECT " +
            "   d.dept_id AS deptId, " +
            "   d.dept_name AS deptName, " +
            "   e.emp_name AS managerName, " +
            "   e.emp_phone AS managerTel, " +
            "   d.create_time AS createTime, " +
            "   d.update_time AS updateTime " +
            "FROM department d " +
            "LEFT JOIN employee e ON d.manager_id = e.emp_id " +
            "ORDER BY d.dept_id asc " +
            "LIMIT #{offset}, #{pageSize} ")
    List<Map<String, Object>> selectDepartmentsWithManagerPage(Integer offset, Integer pageSize);

    /**
     * 统计部门总数
     */
    @Select("SELECT COUNT(*) FROM department")
    Long countDepartments();

    @Delete("DELETE FROM department WHERE dept_id = #{deptId}")
    int deleteById(Integer deptId);
}