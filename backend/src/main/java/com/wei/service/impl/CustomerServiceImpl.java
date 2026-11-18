package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.dto.QueryDto;
import com.wei.entity.Customer;
import com.wei.entity.OperLog;
import com.wei.mapper.CustomerMapper;
import com.wei.mapper.EmployeeMapper;
import com.wei.service.CustomerService;
import com.wei.service.OperLogService;
import com.wei.tools.CustomPage;
import com.wei.tools.SelfTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Resource
    private CustomerMapper mapper;
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private OperLogService operLogService;

    public CustomPage<Map<String, Object>> getCustomer(QueryDto dto) {
        int offset = (dto.getPageNumber() - 1) * dto.getPageSize();
        List<Map<String, Object>> list = mapper.findCustomer();
        SelfTools tools = new SelfTools();

        List<Map<String, Object>> newList = tools.searchCustomer(list, dto.getData());
        HashSet<String> personalGroup = new HashSet<>();
        HashSet<String> empList= new HashSet<>();


        for (Map<String, Object> item : newList) {
            personalGroup.add((String) item.get("personalGroup"));
            empList.add((String) item.get("empName"));
        }

        // 手动分页
        List<Map<String, Object>> pageList = newList.stream()
                .skip(offset)
                .limit(dto.getPageSize())
                .collect(Collectors.toList());

        int total = newList.size();

        // 创建CustomPage对象
        CustomPage<Map<String, Object>> page = new CustomPage<>();

        // 设置分页参数
        page.setCurrent(dto.getPageNumber());   // 当前页码
        page.setSize(dto.getPageSize());        // 每页大小
        page.setTotal(total);                   // 总记录数
        page.setRecords(pageList);              // 分页数据

        // 计算总页数
        page.setPages((int) Math.ceil((double) total / dto.getPageSize()));

        // 设置扩展字段
        page.setTotalGroup(personalGroup.size()); // 总分组数

        HashMap<String, Object> extraMap = new HashMap<>();
        extraMap.put("personalGroup", personalGroup);
        extraMap.put("empList", empList);
        page.setExtraInfo(extraMap);

        return page;
    }

    @Override
    public int deleteCust(Integer custId, String empNumber) {
        // 添加日志
        Customer entity = mapper.getCustomerById(custId);

        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("删除客户");
        operLog.setOperatedTable("customer");
        operLog.setTableId(custId);
        operLog.setOldData(entity.toString());
        operLog.setNewData("无");
        operLogService.addOperLog(operLog);
        return mapper.deletedCustomer(custId);
    }

    @Override
    public boolean save(Customer entity) {
        boolean result = super.save(entity);
        // 添加日志
        String empNumber = (String) employeeMapper.innerGetEmployeeById(entity.getAddedBy()).get("empNumber");
        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("添加客户");
        operLog.setOperatedTable("customer");
        operLog.setTableId(entity.getCustId());
        operLog.setOldData("无");
        operLog.setNewData(entity.toString());
        operLogService.addOperLog(operLog);

        return result;
    }

    @Override
    public boolean updateById(Customer entity) {

        int custId = entity.getCustId();
        Customer customer = mapper.getCustomerById(custId);
        String empNumber = entity.getChangeBy();
        // 添加日志
        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("修改客户信息");
        operLog.setOperatedTable("customer");
        operLog.setTableId(custId);
        operLog.setOldData(customer.toString());
        operLog.setNewData(entity.toString());
        operLogService.addOperLog(operLog);


        return super.updateById(entity);
    }
    public boolean removeCustById(Integer custId,String empNumber){
        OperLog operLog = new OperLog();
        operLog.setOperatorId(empNumber);
        operLog.setOperation("彻底删除客户");
        operLog.setOperatedTable("customer");
        operLog.setTableId(custId);
        operLog.setOldData(mapper.getCustomerById(custId).toString());
        operLog.setNewData("无");
        operLogService.addOperLog(operLog);
        return mapper.removeCustomer(custId)>=1;
    }
}
