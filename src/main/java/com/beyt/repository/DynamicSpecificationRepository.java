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

    List<T> findAll(List<Criteria> criteriaList);

    List<T> findAll(DynamicQuery dynamicQuery);

    Page<T> findAllPage(DynamicQuery dynamicQuery);

    List<Tuple> findAllTuple(DynamicQuery dynamicQuery);

    Page<Tuple> findAllPageTuple(DynamicQuery dynamicQuery);

    <ResultType> List<ResultType> findAll(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);

    <ResultType> Page<ResultType> findAllPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);

    QueryBuilder<T, ID> queryBuilder();

    Page<T> findAll(List<Criteria> criteriaList, Pageable pageable);

    long count(List<Criteria> criteriaList);

    void fetchPartially(ListConsumer<T> processor, int pageSize);

    void fetchPartially(Specification<T> specification, ListConsumer<T> processor, int pageSize);

    void fetchPartially(List<Criteria> criteriaList, ListConsumer<T> processor, int pageSize);
}
