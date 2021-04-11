package com.beyt.filter.query.builder.interfaces;

import java.util.List;

public interface PageableResult<T, ID> {
    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();

    <ResultValue> List<ResultValue> getResult(Class<ResultValue> resultValueClass);
}
