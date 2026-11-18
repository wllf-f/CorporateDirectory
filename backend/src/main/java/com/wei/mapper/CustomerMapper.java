package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.entity.Customer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    @Select("SELECT " +
            "   c.cust_id AS custId, " + // 使用 AS 定义别名
            "   c.cust_name AS custName, " +
            "   c.cust_gender AS custGender, " +
            "   c.cust_phone AS custPhone, " +
            "   c.cust_email AS custEmail, " +
            "   e.emp_name AS empName, " + // 连接表的字段也一并处理
            "   d.dept_name AS deptName, " +
            "   c.is_deleted AS isDeleted, " +
            "   c.privacy_level AS privacyLevel, " +
            "   c.personal_group AS personalGroup, " +
            "   c.create_time AS createTime, " +
            "   c.update_time AS updateTime " +
            "FROM customer c " +
            "LEFT JOIN employee e ON c.added_by = e.emp_id " +
            "LEFT JOIN department d ON e.dept_id = d.dept_id " +
            "ORDER BY c.cust_id ASC ")  // 添加按客户ID升序排序
    List<Map<String, Object>> findCustomer();

    @Select("SELECT * from customer where cust_id=#{custId}")
    public  Customer getCustomerById(Integer custId);



    @Update("UPDATE customer SET is_deleted = '是' WHERE cust_id=#{custId};")
    public int deletedCustomer(Integer custId);

    @Delete("DELETE FROM customer WHERE cust_id= #{custId};")
    public int removeCustomer(Integer custId);
}
