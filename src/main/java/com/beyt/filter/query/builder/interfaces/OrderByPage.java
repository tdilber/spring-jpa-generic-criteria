package com.beyt.filter.query.builder.interfaces;

import com.beyt.dto.enums.Order;
import org.springframework.data.util.Pair;

import java.util.List;

public interface OrderByPage<T, ID> {
    PageResult<T, ID> orderBy(Pair<String, Order>... pairs);

    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();
}
