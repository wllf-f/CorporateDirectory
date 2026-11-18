package com.wei.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.dto.QueryDto;
import com.wei.entity.Customer;
import com.wei.entity.OperLog;
import com.wei.exception.BussinessException;
import com.wei.response.R;
import com.wei.response.ResponseCode;
import com.wei.service.CustomerService;
import com.wei.service.EmployeeService;
import com.wei.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name="客户信息管理")
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;



    @Operation(summary = "查找客户信息")
    @PostMapping("/list")
    public R<Page<Map<String,Object>>> getCustomer(@RequestBody QueryDto data){
        Page<Map<String,Object>> list = customerService.getCustomer(data);
        return R.data(list);
    }

    @Operation(summary = "添加客户")
    @PostMapping("/add")
    public R addCustomer(@RequestBody Customer customer){
        customerService.save(customer);
        return R.success();

    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/delete")
    public R delete(@RequestParam Integer custId,@RequestParam String changeBy){
        int flag = customerService.deleteCust(custId,changeBy);
        if(flag==1){
            return R.success();
        }else{
            throw new BussinessException(ResponseCode.DELETE_FAIL);
        }
    }

    @Operation(summary = "更新客户信息")
    @PutMapping("/update")
    public R update(@RequestBody Customer customer){
        customerService.updateById(customer);
        return R.success();
    }

    @Operation(summary = "彻底删除客户")
    @DeleteMapping("/remove")
    public R remove(@RequestParam Integer custId,@RequestParam String changeBy){
        boolean flag = customerService.removeCustById(custId,changeBy);
        if(flag){
            return R.success();
        }else{
            throw new BussinessException(ResponseCode.DELETE_FAIL);
        }
    }
}
