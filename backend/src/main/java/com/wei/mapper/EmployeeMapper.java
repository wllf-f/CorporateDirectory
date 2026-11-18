package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("SELECT " +
            "   e.emp_id AS empId, " +
            "   e.emp_number AS empNumber, " +
            "   e.emp_name AS empName, " +
            "   e.emp_gender AS empGender, " +
            "   e.emp_phone AS empPhone, " +
            "   e.emp_email AS empEmail, " +
            "   d.dept_name AS deptName, " +
            "   e.emp_role AS empRole, " +
            "   e.password AS password, " +
            "   e.is_active AS isActive, " +
            "   e.create_time AS createTime, " +
            "   e.update_time AS updateTime, " +
            "   e.dept_id AS deptId " +
            "FROM employee e " +
            "LEFT JOIN department d ON e.dept_id = d.dept_id " +
            "ORDER BY e.emp_id asc")
    List<Map<String, Object>> selectEmployeesWithDepartment();

    @Select("SELECT " +
            "   e.emp_id AS empId, " +
            "   e.emp_number AS empNumber, " +
            "   e.emp_name AS empName, " +
            "   e.emp_gender AS empGender, " +
            "   e.emp_phone AS empPhone, " +
            "   e.emp_email AS empEmail, " +
            "   d.dept_name AS deptName, " +
            "   e.emp_role AS empRole, " +
            "   e.password AS password, " +
            "   e.is_active AS isActive, " +
            "   e.create_time AS createTime, " +
            "   e.update_time AS updateTime, " +
            "   e.dept_id AS deptId " +
            "FROM employee e " +
            "LEFT JOIN department d ON e.dept_id = d.dept_id " +
            "WHERE e.emp_id = #{empId}")
    Map<String, Object> innerGetEmployeeById(Integer empId);

    @Select("SELECT " +
            "   e.emp_id AS empId, " +
            "   e.emp_number AS empNumber, " +
            "   e.emp_name AS empName, " +
            "   e.emp_gender AS empGender, " +
            "   e.emp_phone AS empPhone, " +
            "   e.emp_email AS empEmail, " +
            "   d.dept_name AS deptName, " +
            "   e.emp_role AS empRole, " +
            "   e.password AS password, " +
            "   e.is_active AS isActive, " +
            "   e.create_time AS createTime, " +
            "   e.update_time AS updateTime, " +
            "   e.dept_id AS deptId " +
            "FROM employee e " +
            "LEFT JOIN department d ON e.dept_id = d.dept_id " +
            "WHERE e.emp_number = #{empNumber} " +
            "ORDER BY e.emp_id ASC")
    Map<String, Object> innerGetEmployeeByNumber(String empNumber);

    @Update("UPDATE employee SET is_active = '离职' WHERE emp_id = #{empId};")
    int deleteEmployee(Integer empId);

    @Select("SELECT " +
            "   (SELECT COUNT(*) FROM employee) AS empTotal, " +
            "   (SELECT COUNT(*) FROM department) AS deptTotal, " +
            "   (SELECT COUNT(*) FROM customer) AS custTotal")
    Map<String, Integer> getTotal();

    @Delete("DELETE FROM employee WHERE emp_id = #{empId}")
    int deleteEmployeeById(Integer empId);

    @Insert("INSERT INTO employee (emp_number, emp_name, emp_gender, emp_phone, emp_email, dept_id, emp_role, password, is_active) VALUES (#{empNumber}, #{empName}, #{empGender}, #{empPhone}, #{empEmail}, #{deptId}, #{empRole}, #{password}, #{isActive})")
    int saveEmployee(Employee entity);
}