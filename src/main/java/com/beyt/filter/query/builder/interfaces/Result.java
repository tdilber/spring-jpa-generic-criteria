package com.beyt.filter.query.builder.interfaces;

import java.util.List;

public interface Result<T, ID> {
    List<T> getResult();
}

