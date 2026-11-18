package com.wei.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("oper_log")
@Schema(description = "日志实体类")
public class OperLog {
    /**
     * 主键日志ID
     */
    @Schema(description = "日志ID")
    @TableId(type = IdType.AUTO)
    private Integer loginId;
    /**
     * 操作人工号
     */
    @Schema(description = "操作人工号")
    private String operatorId;
    /**
     * 操作内容
     */
    @Schema(description = "操作类型")
    private String operation;
    /**
     * 操作表
     */
    @Schema(description = "操作表")
    private String operatedTable;
    /**
     * 表主键id
     */
    @Schema(description = "表主键id")
    private Integer tableId;
    /**
     * 旧数据
     */
    @Schema(description = "旧数据")
    private String oldData;
    /**
     * 新数据
     */
    @Schema(description = "新数据")
    private String newData;
    /**
     * 操作时间
     */
    @Schema(description = "操作时间")
    private String operDate;

    @Override
    public String toString() {
        return "OperLog{" +
                "loginId=" + loginId +
                ", empId=" + operatorId +
                ", operation='" + operation + '\'' +
                ", operatedTable='" + operatedTable + '\'' +
                ", tableId=" + tableId +
                ", oldData='" + oldData + '\'' +
                ", newData='" + newData + '\'' +
                ", operDate='" + operDate + '\'' +
                '}';
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getOperator_id() {
        return operatorId;
    }

    public void setOperator_id(String operator_id) {
        this.operatorId = operator_id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperatedTable() {
        return operatedTable;
    }

    public void setOperatedTable(String operatedTable) {
        this.operatedTable = operatedTable;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }


    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }
}
