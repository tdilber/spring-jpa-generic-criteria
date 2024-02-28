package com.beyt.jdq.repository;

import com.beyt.jdq.dto.Criteria;
import com.beyt.jdq.dto.DynamicQuery;
import com.beyt.jdq.query.DynamicQueryManager;
import com.beyt.jdq.query.builder.QueryBuilder;
import com.beyt.jdq.util.ListConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Tuple;
import java.util.List;

@NoRepositoryBean
public interface JpaDynamicQueryRepository<T, ID> extends DynamicSpecificationRepository<T, ID> {

    default List<T> findAll(List<Criteria> criteriaList) {
        return DynamicQueryManager.findAll(this, criteriaList);
    }

    default List<T> findAll(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterAsList(this, dynamicQuery);
    }

    default Page<T> findAllPage(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterAsPage(this, dynamicQuery);
    }

    default List<Tuple> findAllTuple(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithTupleAsList(this, dynamicQuery);
    }

    default Page<Tuple> findAllPageTuple(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithTupleAsPage(this, dynamicQuery);
    }

    default <ResultType> List<ResultType> findAll(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithReturnTypeAsList(this, dynamicQuery, resultTypeClass);
    }

    default <ResultType> Page<ResultType> findAllPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithReturnTypeAsPage(this, dynamicQuery, resultTypeClass);
    }

    default QueryBuilder<T, ID> queryBuilder() {
        return new QueryBuilder<T, ID>(this);
    }

    default Page<T> findAll(List<Criteria> criteriaList, Pageable pageable) {
        return DynamicQueryManager.findAll(this, criteriaList, pageable);
    }

    static <T> Specification<T> getSpecificationWithCriteria(List<Criteria> criteriaList) {
        return DynamicQueryManager.getSpecification(criteriaList);
    }

    default long count(List<Criteria> criteriaList) {
        return DynamicQueryManager.count(this, criteriaList);
    }

    default void fetchPartially(ListConsumer<T> processor, int pageSize) {
        fetchPartially((Specification<T>) null, processor, pageSize);
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

    default void fetchPartially(List<Criteria> criteriaList, ListConsumer<T> processor, int pageSize) {
        long totalElements = DynamicQueryManager.count(this, criteriaList);

        for (int i = 0; (long) i * pageSize < totalElements; i++) {
            processor.accept(DynamicQueryManager.findAll(this, criteriaList, PageRequest.of(i, pageSize)).getContent());
        }
    }
}
