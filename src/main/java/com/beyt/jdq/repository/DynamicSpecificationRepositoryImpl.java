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
public class DynamicSpecificationRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DynamicSpecificationRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    private final EntityManager entityManager;

    public DynamicSpecificationRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                              EntityManager entityManager) {

        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> findAll(List<Criteria> criteriaList) {
        return DynamicQueryManager.findAll(this, criteriaList);
    }

    @Override
    public List<T> findAll(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterAsList(this, dynamicQuery);
    }

    @Override
    public Page<T> findAllPage(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterAsPage(this, dynamicQuery);
    }

    @Override
    public List<Tuple> findAllTuple(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithTupleAsList(this, dynamicQuery);
    }

    @Override
    public Page<Tuple> findAllPageTuple(DynamicQuery dynamicQuery) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithTupleAsPage(this, dynamicQuery);
    }

    @Override
    public <ResultType> List<ResultType> findAll(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithReturnTypeAsList(this, dynamicQuery, resultTypeClass);
    }

    @Override
    public <ResultType> Page<ResultType> findAllPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass) {
        return DynamicQueryManager.getEntityListBySelectableFilterWithReturnTypeAsPage(this, dynamicQuery, resultTypeClass);
    }

    @Override
    public QueryBuilder<T, ID> queryBuilder() {
        return new QueryBuilder<>(this);
    }

    @Override
    public Page<T> findAll(List<Criteria> criteriaList, Pageable pageable) {
        return DynamicQueryManager.findAll(this, criteriaList, pageable);
    }

    static <T> Specification<T> getSpecificationWithCriteria(List<Criteria> criteriaList) {
        return DynamicQueryManager.getSpecification(criteriaList);
    }

    public Class<T> getDomainClass() {
        return super.getDomainClass();
    }

    @Override
    public long count(List<Criteria> criteriaList) {
        return DynamicQueryManager.count(this, criteriaList);
    }

    @Override
    public void fetchPartially(ListConsumer<T> processor, int pageSize) {
        fetchPartially((Specification<T>) null, processor, pageSize);
    }

    @Override
    public void fetchPartially(Specification<T> specification, ListConsumer<T> processor, int pageSize) {
        Page<T> page = this.findAll((Specification<T>) null, PageRequest.of(0, pageSize));
        processor.accept(page.getContent());
        long totalElements = page.getTotalElements();
        for (int i = 1; (long) i * pageSize < totalElements; i++) {
            page = this.findAll(specification, PageRequest.of(i, pageSize));
            processor.accept(page.getContent());
        }
    }

    @Override
    public void fetchPartially(List<Criteria> criteriaList, ListConsumer<T> processor, int pageSize) {
        long totalElements = DynamicQueryManager.count(this, criteriaList);

        for (int i = 0; (long) i * pageSize < totalElements; i++) {
            processor.accept(DynamicQueryManager.findAll(this, criteriaList, PageRequest.of(i, pageSize)).getContent());
        }
    }
}
