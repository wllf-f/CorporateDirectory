package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.dto.QueryDto;
import com.wei.entity.OperLog;
import com.wei.mapper.OperLogMapper;
import com.wei.service.OperLogService;
import com.wei.tools.SelfTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

    @Resource
    private OperLogMapper operLogMapper;

    @Override
    public Page<Map<String, Object>> getAllOperLogs(QueryDto dto){
        int offset = (dto.getPageNumber() - 1) * dto.getPageSize();

        // 获取所有记录
        List<Map<String, Object>> records = operLogMapper.getAllOperLogs();

        List<Map<String, Object>> newRecords = new ArrayList<>();

        // 搜索
        SelfTools tools = new SelfTools();
        newRecords = tools.searchCustomer(records,dto.getData());

        // 手动分页
        List<Map<String, Object>> pageList = newRecords.stream()
                .skip(offset)
                .limit(dto.getPageSize())
                .collect(Collectors.toList());

        int total = newRecords.size();

        Page<Map<String, Object>> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        page.setRecords(pageList);
        page.setTotal(total);
        return page;
    }
    public boolean addOperLog(OperLog operLog) {

        // 使用BaseMapper的insert方法
        int result = operLogMapper.insert(operLog);
        return result > 0;
    }
}
