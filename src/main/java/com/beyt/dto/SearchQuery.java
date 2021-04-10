package com.beyt.dto;

import com.beyt.dto.enums.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tdilber at 30-Dec-20
 */
@Slf4j
public class SearchQuery {

    private boolean distinct = false;
    private Integer pageSize = null;
    private Integer pageNumber = null;
    private List<String> select = new ArrayList<>();
    private List<Criteria> where = new CriteriaFilter();
    private Map<String, Order> orderBy = new HashMap<>();

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<String> getSelect() {
        return select;
    }

    public void setSelect(List<String> select) {
        this.select = select;
    }

    public List<Criteria> getWhere() {
        return where;
    }

    public void setWhere(List<Criteria> where) {
        this.where = where;
    }

    public Map<String, Order> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, Order> orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
