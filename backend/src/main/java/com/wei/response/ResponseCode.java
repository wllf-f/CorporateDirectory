package com.wei.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "成功"),
    ERROR(500, "服务器错误"),
    USERNAME_OR_PASSWORD_ERROR(500, "用户名或密码错误"),
    USERNAME_EXIST(500, "用户名已存在"),
    DEPTNAME_EXIST(10086, "部门名称已存在"),
    DEPT_NOT_EXIST(10087, "部门不存在"),
    DELETE_FAIL(10088,"删除失败");


    private Integer code;
    private String message;

}