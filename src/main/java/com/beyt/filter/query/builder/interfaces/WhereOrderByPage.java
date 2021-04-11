package com.beyt.filter.query.builder.interfaces;

import com.beyt.dto.Criteria;
import com.beyt.filter.query.simplifier.QuerySimplifier;

import java.util.List;

public interface WhereOrderByPage<T, ID> {
    OrderByPage<T, ID> where(Criteria... criteria);

    PageableResult<T, ID> orderBy(QuerySimplifier.OrderByRule... pairs);

    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();

    <ResultValue> List<ResultValue> getResult(Class<ResultValue> resultValueClass);
}

