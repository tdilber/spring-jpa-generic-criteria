package com.beyt.filter.query.builder.interfaces;

import java.util.List;

public interface PageResult<T, ID> {
    Result<T, ID> page(int pageNumber, int pageSize);

    List<T> getResult();
}
