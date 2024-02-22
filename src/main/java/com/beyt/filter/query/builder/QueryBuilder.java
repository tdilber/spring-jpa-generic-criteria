package com.beyt.filter.query.builder;

import com.beyt.dto.Criteria;
import com.beyt.dto.DynamicQuery;
import com.beyt.filter.DatabaseFilterManager;
import com.beyt.filter.query.builder.interfaces.*;
import com.beyt.filter.query.simplifier.QuerySimplifier;
import com.beyt.repository.GenericSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class QueryBuilder<T, ID> implements DistinctWhereOrderByPage<T, ID>, WhereOrderByPage<T, ID>, OrderByPage<T, ID>, PageableResult<T, ID>, Result<T, ID> {
    protected final GenericSpecificationRepository<T, ID> genericSpecificationRepository;
    protected final DynamicQuery dynamicQuery;

    public QueryBuilder(GenericSpecificationRepository<T, ID> genericSpecificationRepository) {
        this.genericSpecificationRepository = genericSpecificationRepository;
        dynamicQuery = new DynamicQuery();
    }

    public DistinctWhereOrderByPage<T, ID> select(QuerySimplifier.SelectRule... selectRules) {
        dynamicQuery.getSelect().addAll(Arrays.stream(selectRules).map(o -> Pair.of(o.getFieldName(), o.getAlias())).collect(Collectors.toList()));
        return this;
    }

    public WhereOrderByPage<T, ID> distinct(boolean distinct) {
        dynamicQuery.setDistinct(distinct);
        return this;
    }


    public OrderByPage<T, ID> where(Criteria... criteria) {
        dynamicQuery.getWhere().addAll(Arrays.asList(criteria));
        return this;
    }


    public PageableResult<T, ID> orderBy(QuerySimplifier.OrderByRule... pairs) {
        dynamicQuery.getOrderBy().addAll(Arrays.stream(pairs).map(o -> Pair.of(o.getFieldName(), o.getOrderType())).collect(Collectors.toList()));
        return this;
    }

    public Result<T, ID> page(int pageNumber, int pageSize) {
        dynamicQuery.setPageSize(pageSize);
        dynamicQuery.setPageNumber(pageNumber);
        return this;
    }

    public List<T> getResult() {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsList(genericSpecificationRepository, dynamicQuery);
    }

    public <ResultValue> List<ResultValue> getResult(Class<ResultValue> resultValueClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsList(genericSpecificationRepository, dynamicQuery, resultValueClass);
    }

    public Page<T> getResultAsPage() {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsPage(genericSpecificationRepository, dynamicQuery);
    }

    public <ResultValue> Page<ResultValue> getResultAsPage(Class<ResultValue> resultValueClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsPage(genericSpecificationRepository, dynamicQuery, resultValueClass);
    }
}
