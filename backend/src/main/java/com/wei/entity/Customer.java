package com.wei.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("customer")
@Schema(description = "客户信息实体类")
public class Customer {
    /**
     * 客户ID
     */
    @Schema(description = "客户ID")
    @TableId(type = IdType.AUTO)
    private Integer custId;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称")
    private String custName;
    /**
     * 客户性别
     */
    @Schema(description = "客户性别")
    private String custGender;
    /**
     * 客户电话
     */
    @Schema(description = "客户电话")
    private String custPhone;
    /**
     * 客户邮箱
     */
    @Schema(description = "客户邮箱")
    private String custEmail;
    /**
     * 添加员工id
     */
    @Schema(description = "添加员工id")
    private Integer addedBy;
    /**
     * 是否删除
     */
     @Schema(description = "是否删除")
    private String isDeleted;
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
     * 隐私等级
      */
     @Schema(description = "隐私等级")
    private String privacyLevel;
    /**
     * 添加员工姓名
     */
    @TableField(exist=false)
    @Schema(description = "添加员工姓名")
    private String empName;

    /**
     *客户所负责部门
     */
    @TableField(exist=false)
    @Schema(description = "客户所负责部门")
    private String deptName;
    /**
     *分组
     */
    @Schema(description = "分组")
    private String personalGroup;

    /**
     * 修改工号
     */
    @TableField(exist=false)
    @Schema(description = "操作工号")
    private String changeBy;

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custGender='" + custGender + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custEmail='" + custEmail + '\'' +
                ", addedBy=" + addedBy +
                ", isDeleted='" + isDeleted + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", privacyLevel='" + privacyLevel + '\'' +
                ", empName='" + empName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", personalGroup='" + personalGroup + '\'' +
                '}';
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(String privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPersonalGroup() {
        return personalGroup;
    }

    public void setPersonalGroup(String personalGroup) {
        this.personalGroup = personalGroup;
    }

    public String getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(String changeBy) {
        this.changeBy = changeBy;
    }
}
