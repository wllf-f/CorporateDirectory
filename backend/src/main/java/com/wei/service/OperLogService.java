package com.wei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.dto.QueryDto;
import com.wei.entity.OperLog;

import java.util.Map;

public interface OperLogService extends IService<OperLog> {
    Page<Map<String, Object>>  getAllOperLogs(QueryDto dto);

    boolean addOperLog(OperLog operLog);


}
