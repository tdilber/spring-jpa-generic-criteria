package com.beyt.dto;

import com.beyt.dto.enums.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tdilber at 30-Dec-20
 */
@Slf4j
public class DynamicQuery {

    private boolean distinct = false;
    private Integer pageSize = null;
    private Integer pageNumber = null;
    private List<Pair<String, String>> select = new ArrayList<>();
    private List<Criteria> where = new CriteriaList();
    private List<Pair<String, Order>> orderBy = new ArrayList<>();

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Pair<String, String>> getSelect() {
        return select;
    }

    public void setSelect(List<Pair<String, String>> select) {
        this.select = select;
    }

    public List<Criteria> getWhere() {
        return where;
    }

    public void setWhere(List<Criteria> where) {
        this.where = where;
    }

    public List<Pair<String, Order>> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<Pair<String, Order>> orderBy) {
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
