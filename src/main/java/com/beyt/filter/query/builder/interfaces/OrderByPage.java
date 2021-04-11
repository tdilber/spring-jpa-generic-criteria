package com.beyt.filter.query.builder.interfaces;

import com.beyt.filter.query.simplifier.QuerySimplifier;

import java.util.List;

public interface OrderByPage<T, ID> {
    PageableResult<T, ID> orderBy(QuerySimplifier.OrderByRule... pairs);

    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();

    <ResultValue> List<ResultValue> getResult(Class<ResultValue> resultValueClass);
}
