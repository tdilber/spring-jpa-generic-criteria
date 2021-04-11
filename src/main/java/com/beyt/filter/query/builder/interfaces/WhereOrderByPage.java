package com.beyt.filter.query.builder.interfaces;

import com.beyt.dto.Criteria;
import com.beyt.dto.enums.Order;
import org.springframework.data.util.Pair;

import java.util.List;

public interface WhereOrderByPage<T, ID> {
    OrderByPage<T, ID> where(Criteria... criteria);

    PageResult<T, ID> orderBy(Pair<String, Order>... pairs);

    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();
}

