package com.beyt.filter.query.builder;

import com.beyt.dto.Criteria;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.Order;
import com.beyt.filter.DatabaseFilterManager;
import com.beyt.filter.query.builder.interfaces.*;
import com.beyt.repository.JpaExtendedRepository;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QueryBuilder<T, ID> implements DistinctWhereOrderByPage<T, ID>, WhereOrderByPage<T, ID>, OrderByPage<T, ID>, PageResult<T, ID>, Result<T, ID> {
    private JpaExtendedRepository<T, ID> jpaExtendedRepository;
    private SearchQuery searchQuery;

    public QueryBuilder(JpaExtendedRepository<T, ID> jpaExtendedRepository) {
        this.jpaExtendedRepository = jpaExtendedRepository;
        searchQuery = new SearchQuery();
    }

    public DistinctWhereOrderByPage<T, ID> select(String... selects) {
        searchQuery.getSelect().addAll(Arrays.asList(selects));
        return this;
    }

    public WhereOrderByPage<T, ID> distinct(boolean distinct) {
        searchQuery.setDistinct(distinct);
        return this;
    }


    public OrderByPage<T, ID> where(Criteria... criteria) {
        searchQuery.getWhere().addAll(Arrays.asList(criteria));
        return this;
    }


    public PageResult<T, ID> orderBy(Pair<String, Order>... pairs) {
        HashMap<String, Order> orderBy = new HashMap<>();
        Arrays.stream(pairs).forEach(p -> orderBy.put(p.getFirst(), p.getSecond()));
        searchQuery.getOrderBy().putAll(orderBy);
        return this;
    }

    public Result<T, ID> page(int pageNumber, int pageSize) {
        searchQuery.setPageSize(pageSize);
        searchQuery.setPageNumber(pageNumber);
        return this;
    }

    public List<T> getResult() {
        return DatabaseFilterManager.getEntityListBySelectableFilter(jpaExtendedRepository, searchQuery);
    }
}
