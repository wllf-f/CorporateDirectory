package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.dto.QueryDto;
import com.wei.response.R;
import com.wei.response.ResponseCode;
import com.wei.service.EmployeeService;
import com.wei.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "日志")
@RequestMapping("/operLog")
public class OperLogController {
    @Resource
    private OperLogService operLogService;

    @PostMapping("/list")
    public R<Page<Map<String, Object>>> list(
            @RequestBody QueryDto dto) {
        Page<Map<String, Object>> page = operLogService.getAllOperLogs(dto);
        return R.data(page);
    }
}
