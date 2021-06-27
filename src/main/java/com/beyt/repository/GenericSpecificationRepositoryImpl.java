package com.beyt.repository;

import com.beyt.dto.Criteria;
import com.beyt.dto.IFetchPartiallyProcessor;
import com.beyt.dto.SearchQuery;
import com.beyt.filter.DatabaseFilterManager;
import com.beyt.filter.query.builder.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public class GenericSpecificationRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericSpecificationRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    private final EntityManager entityManager;

    public GenericSpecificationRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                              EntityManager entityManager) {

        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> findAllWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.findAll(this, criteriaList);
    }

    @Override
    public List<T> findAllWithSearchQuery(SearchQuery searchQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsList(this, searchQuery);
    }

    @Override
    public Page<T> findAllWithSearchQueryAsPage(SearchQuery searchQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterAsPage(this, searchQuery);
    }

    @Override
    public List<Tuple> findAllWithSearchQueryWithTuple(SearchQuery searchQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithTupleAsList(this, searchQuery);
    }

    @Override
    public Page<Tuple> findAllWithSearchQueryWithTupleAsPage(SearchQuery searchQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithTupleAsPage(this, searchQuery);
    }

    @Override
    public <ResultType> List<ResultType> findAllWithSearchQuery(SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsList(this, searchQuery, resultTypeClass);
    }

    @Override
    public <ResultType> Page<ResultType> findAllWithSearchQueryAsPage(SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilterWithReturnTypeAsPage(this, searchQuery, resultTypeClass);
    }

    @Override
    public QueryBuilder<T, ID> query() {
        return new QueryBuilder<>(this);
    }

    @Override
    public Page<T> findAllWithCriteria(List<Criteria> criteriaList, Pageable pageable) {
        return DatabaseFilterManager.findAll(this, criteriaList, pageable);
    }

    static <T> Specification<T> getSpecificationWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.getSpecification(criteriaList);
    }

    public Class<T> getDomainClass() {
        return super.getDomainClass();
    }

    @Override
    public long countWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.count(this, criteriaList);
    }

    @Override
    public void fetchPartially(IFetchPartiallyProcessor<T> processor, int pageSize) {
        fetchPartially(null, processor, pageSize);
    }

    @Override
    public void fetchPartially(Specification<T> specification, IFetchPartiallyProcessor<T> processor, int pageSize) {
        Page<T> page = this.findAll((Specification<T>) null, PageRequest.of(0, pageSize));
        processor.process(page.getContent());
        long totalElements = page.getTotalElements();
        for (int i = 1; (long) i * pageSize < totalElements; i++) {
            page = this.findAll(specification, PageRequest.of(i, pageSize));
            processor.process(page.getContent());
        }
    }

    @Override
    public void fetchPartiallyWithCriteria(List<Criteria> criteriaList, IFetchPartiallyProcessor<T> processor, int pageSize) {
        long totalElements = DatabaseFilterManager.count(this, criteriaList);

        for (int i = 0; (long) i * pageSize < totalElements; i++) {
            processor.process(DatabaseFilterManager.findAll(this, criteriaList, PageRequest.of(i, pageSize)).getContent());
        }
    }
}
