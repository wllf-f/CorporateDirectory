package com.wei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("employee")
@Schema(description = "员工信息实体类")
public class Employee {
    /**
     * 主键员工ID
     */
    @Schema(description = "员工ID")
    @TableId(type = IdType.AUTO)
    private Integer empId;

    /**
     * 员工工号
     */
    @Schema(description = "员工工号")
    private String empNumber;
    /**
     * 员工姓名
     */
    @Schema(description = "员工姓名")
    private String empName;
    /**
     * 员工性别
     */
    @Schema(description = "员工性别")
    private String empGender;
    /**
     * 员工电话
     */
    @Schema(description = "员工电话")
    private String empPhone;
    /**
     * 员工邮箱
     */
    @Schema(description = "员工邮箱")
    private String empEmail;
    /**
     * 员工部门ID
     */
    @Schema(description = "员工部门ID")
    private Integer deptId;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    @Schema(description = "部门名称")
    private String deptName;
    /**
     * 员工身份
     */
    @Schema(description = "员工身份")
    private String empRole;
    /**
     * 员工密码
     */
    @Schema(description = "员工密码")
    private String password;
    /**
     * 员工状态
     */
    @Schema(description = "员工状态")
    private String isActive;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private String CreateTime;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private String UpdateTime;
    /**
     * 部门操作工号
     */
    @TableField(exist = false)
    @Schema(description = "操作工号")
    private String changeBy;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String empRole) {
        this.empRole = empRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(String changeBy) {
        this.changeBy = changeBy;
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", empNumber='" + empNumber + '\'' +
                ", empName='" + empName + '\'' +
                ", empGender='" + empGender + '\'' +
                ", empPhone='" + empPhone + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", empRole='" + empRole + '\'' +
                ", password='" + password + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
