package com.beyt.repository;

import com.beyt.dto.Criteria;
import com.beyt.dto.DynamicQuery;
import com.beyt.filter.DatabaseFilterManager;
import com.beyt.filter.query.builder.QueryBuilder;
import com.beyt.util.ListConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Tuple;
import java.util.List;

@NoRepositoryBean
public interface JpaDynamicQueryRepository<T, ID> extends GenericSpecificationRepository<T, ID> {

    default List<T> findAllWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.findAll(this, criteriaList);
    }

    default List<T> findAllWithSearchQuery(DynamicQuery dynamicQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsList(this, dynamicQuery);
    }

    default Page<T> findAllWithSearchQueryAsPage(DynamicQuery dynamicQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsPage(this, dynamicQuery);
    }

    default List<Tuple> findAllWithSearchQueryWithTuple(DynamicQuery dynamicQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithTupleAsList(this, dynamicQuery);
    }

    default Page<Tuple> findAllWithSearchQueryWithTupleAsPage(DynamicQuery dynamicQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithTupleAsPage(this, dynamicQuery);
    }

    default <ResultType> List<ResultType> findAllWithSearchQuery(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsList(this, dynamicQuery, resultTypeClass);
    }

    default <ResultType> Page<ResultType> findAllWithSearchQueryAsPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsPage(this, dynamicQuery, resultTypeClass);
    }

    default QueryBuilder<T, ID> query() {
        return new QueryBuilder<T, ID>(this);
    }

    default Page<T> findAllWithCriteria(List<Criteria> criteriaList, Pageable pageable) {
        return DatabaseFilterManager.findAll(this, criteriaList, pageable);
    }

    static <T> Specification<T> getSpecificationWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.getSpecification(criteriaList);
    }

    default long countWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.count(this, criteriaList);
    }

    default void fetchPartially(ListConsumer<T> processor, int pageSize) {
        fetchPartially(null, processor, pageSize);
    }

    default void fetchPartially(Specification<T> specification, ListConsumer<T> processor, int pageSize) {
        Page<T> page = this.findAll((Specification<T>) null, PageRequest.of(0, pageSize));
        processor.accept(page.getContent());
        long totalElements = page.getTotalElements();
        for (int i = 1; (long) i * pageSize < totalElements; i++) {
            page = this.findAll(specification, PageRequest.of(i, pageSize));
            processor.accept(page.getContent());
        }
    }

    default void fetchPartiallyWithCriteria(List<Criteria> criteriaList, ListConsumer<T> processor, int pageSize) {
        long totalElements = DatabaseFilterManager.count(this, criteriaList);

        for (int i = 0; (long) i * pageSize < totalElements; i++) {
            processor.accept(DatabaseFilterManager.findAll(this, criteriaList, PageRequest.of(i, pageSize)).getContent());
        }
    }
}
