package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.entity.OperLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

    @Select("SELECT " +
            "   login_id AS loginId, " +
            "   operator_id AS operatorId, " +
            "   operation AS operation, " +
            "   operated_table AS operatedTable, " +
            "   table_id AS tableId, " +
            "   old_data AS oldData, " +
            "   new_data AS newData, " +
            "   oper_date AS operDate " +
            "FROM oper_log " +
            "ORDER BY login_id ASC")
    List<Map<String, Object>> getAllOperLogs();

    @Insert("INSERT INTO oper_log (operator_id, operation, operated_table, table_id, old_data, new_data) " +
            "VALUES (#{operatorId}, #{operation}, #{operatedTable}, #{tableId}, #{oldData}, #{newData})")
    boolean insertOperLog(OperLog operLog);
}