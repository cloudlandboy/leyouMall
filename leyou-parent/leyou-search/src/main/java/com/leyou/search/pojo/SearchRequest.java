package com.leyou.search.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装页面搜索请求参数
 */
public class SearchRequest {
    /**
     * 搜索关键字
     */
    private String key;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 排序方式
     */
    private String sortBy;

    /**
     * 是否降序
     */
    private Boolean descending;

    /**
     * 搜索过滤
     */
    private Map<String, String> filters = new HashMap<>();

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    /**
     * 每页大小，不从页面接收，而是固定大小
     */
    private static final Integer DEFAULT_SIZE = 20;

    /**
     * 默认页
     */
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}