package com.beyt.repository;

import com.beyt.dto.Criteria;
import com.beyt.dto.DynamicQuery;
import com.beyt.query.builder.QueryBuilder;
import com.beyt.util.ListConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Tuple;
import java.util.List;

@NoRepositoryBean
public interface DynamicSpecificationRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    List<T> findAllWithCriteria(List<Criteria> criteriaList);

    List<T> findAllWithSearchQuery(DynamicQuery dynamicQuery);

    Page<T> findAllWithSearchQueryAsPage(DynamicQuery dynamicQuery);

    List<Tuple> findAllWithSearchQueryWithTuple(DynamicQuery dynamicQuery);

    Page<Tuple> findAllWithSearchQueryWithTupleAsPage(DynamicQuery dynamicQuery);

    <ResultType> List<ResultType> findAllWithSearchQuery(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);

    <ResultType> Page<ResultType> findAllWithSearchQueryAsPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);

    QueryBuilder<T, ID> query();

    Page<T> findAllWithCriteria(List<Criteria> criteriaList, Pageable pageable);

    long countWithCriteria(List<Criteria> criteriaList);

    void fetchPartially(ListConsumer<T> processor, int pageSize);

    void fetchPartially(Specification<T> specification, ListConsumer<T> processor, int pageSize);

    void fetchPartiallyWithCriteria(List<Criteria> criteriaList, ListConsumer<T> processor, int pageSize);
}
