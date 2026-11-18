package com.wei.tools;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;

public class CustomPage<T> extends Page<T> {
    private Integer totalGroup; // 总分组数
    private Map<String, Object> extraInfo; // 额外信息

    // 修正构造方法
    public CustomPage(long current, long size, long total) {
        super(current, size, total);
    }

    // 简化构造方法（可选）
    public CustomPage(long current, long size) {
        super(current, size);
    }

    // 无参构造器
    public CustomPage() {
        super();
    }

    // getter和setter
    public Integer getTotalGroup() {
        return totalGroup;
    }

    public void setTotalGroup(Integer totalGroup) {
        this.totalGroup = totalGroup;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }
}