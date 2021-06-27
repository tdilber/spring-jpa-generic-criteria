package com.beyt.repository;

import com.beyt.dto.Criteria;
import com.beyt.dto.IFetchPartiallyProcessor;
import com.beyt.dto.SearchQuery;
import com.beyt.filter.query.builder.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Tuple;
import java.util.List;

@NoRepositoryBean
public interface GenericSpecificationRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    List<T> findAllWithCriteria(List<Criteria> criteriaList);

    List<T> findAllWithSearchQuery(SearchQuery searchQuery);

    Page<T> findAllWithSearchQueryAsPage(SearchQuery searchQuery);

    List<Tuple> findAllWithSearchQueryWithTuple(SearchQuery searchQuery);

    Page<Tuple> findAllWithSearchQueryWithTupleAsPage(SearchQuery searchQuery);

    <ResultType> List<ResultType> findAllWithSearchQuery(SearchQuery searchQuery, Class<ResultType> resultTypeClass);

    <ResultType> Page<ResultType> findAllWithSearchQueryAsPage(SearchQuery searchQuery, Class<ResultType> resultTypeClass);

    QueryBuilder<T, ID> query();

    Page<T> findAllWithCriteria(List<Criteria> criteriaList, Pageable pageable);

    long countWithCriteria(List<Criteria> criteriaList);

    void fetchPartially(IFetchPartiallyProcessor<T> processor, int pageSize);

    void fetchPartially(Specification<T> specification, IFetchPartiallyProcessor<T> processor, int pageSize);

    void fetchPartiallyWithCriteria(List<Criteria> criteriaList, IFetchPartiallyProcessor<T> processor, int pageSize);
}
