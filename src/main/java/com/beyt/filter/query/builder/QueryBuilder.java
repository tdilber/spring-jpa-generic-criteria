package com.beyt.filter.query.builder;

import com.beyt.dto.Criteria;
import com.beyt.dto.SearchQuery;
import com.beyt.filter.DatabaseFilterManager;
import com.beyt.filter.query.builder.interfaces.*;
import com.beyt.filter.query.simplifier.QuerySimplifier;
import com.beyt.repository.JpaExtendedRepository;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class QueryBuilder<T, ID> implements DistinctWhereOrderByPage<T, ID>, WhereOrderByPage<T, ID>, OrderByPage<T, ID>, PageableResult<T, ID>, Result<T, ID> {
    protected final JpaExtendedRepository<T, ID> jpaExtendedRepository;
    protected final SearchQuery searchQuery;

    public QueryBuilder(JpaExtendedRepository<T, ID> jpaExtendedRepository) {
        this.jpaExtendedRepository = jpaExtendedRepository;
        searchQuery = new SearchQuery();
    }

    public DistinctWhereOrderByPage<T, ID> select(QuerySimplifier.SelectRule... selectRules) {
        searchQuery.getSelect().addAll(Arrays.stream(selectRules).map(o -> Pair.of(o.getFieldName(), o.getAlias())).collect(Collectors.toList()));
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


    public PageableResult<T, ID> orderBy(QuerySimplifier.OrderByRule... pairs) {
        searchQuery.getOrderBy().addAll(Arrays.stream(pairs).map(o -> Pair.of(o.getFieldName(), o.getOrderType())).collect(Collectors.toList()));
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

    public <ResultValue> List<ResultValue> getResult(Class<ResultValue> resultValueClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnType(jpaExtendedRepository, searchQuery, resultValueClass);
    }
}
