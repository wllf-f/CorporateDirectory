package com.wei.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装的customer的requestbody查找类
 */
public class QueryDto {
    private Integer pageNumber = 1;
    private Integer pageSize = 10;
    private Map<String,Object> data = new HashMap<>();

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNum) {
        this.pageNumber = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
