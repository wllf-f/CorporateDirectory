package com.wei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.dto.QueryDto;
import com.wei.entity.Customer;

import java.util.Map;

public interface CustomerService extends IService<Customer> {
    Page<Map<String,Object>> getCustomer(QueryDto dto);

    int deleteCust(Integer custId,String empNumber);

    boolean removeCustById(Integer custId,String empNumber);
}
