package com.beyt.repository;

import com.beyt.dto.Criteria;
import com.beyt.dto.IFetchPartiallyProcessor;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.filter.DatabaseFilterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;

@NoRepositoryBean
public interface JpaExtendedRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Autowired
    EntityManager entityManagerrr = null;

    default List<T> findAllWithCriteria(List<Criteria> criteriaList) {
        return DatabaseFilterManager.findAll(this, criteriaList);
    }

    default <Entity> List<Tuple> findAllWithSearchQuery(SearchQuery searchQuery) {
        return DatabaseFilterManager.getEntityListBySelectableFilter(this, searchQuery);
    }
    default <Entity, ResultType> List<ResultType> findAllWithSearchQuery(SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        return DatabaseFilterManager.getEntityListBySelectableFilter(this, searchQuery, resultTypeClass);
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

    default void fetchPartially(IFetchPartiallyProcessor<T> processor, int pageSize) {
        fetchPartially(null, processor, pageSize);
    }

    default void fetchPartially(Specification<T> specification, IFetchPartiallyProcessor<T> processor, int pageSize) {
        Page<T> page = this.findAll((Specification<T>) null, PageRequest.of(0, pageSize));
        processor.process(page.getContent());
        long totalElements = page.getTotalElements();
        for (int i = 1; (long) i * pageSize < totalElements; i++) {
            page = this.findAll(specification, PageRequest.of(i, pageSize));
            processor.process(page.getContent());
        }
    }

    default void fetchPartiallyWithCriteria(List<Criteria> criteriaList, IFetchPartiallyProcessor<T> processor, int pageSize) {
        long totalElements = DatabaseFilterManager.count(this, criteriaList);

        Criteria criteria = new Criteria("id", CriteriaType.PAGE_ASC, pageSize, 0);
        criteriaList.add(criteria);
        for (int i = 0; (long) i * pageSize < totalElements; i++) {
            criteria.values.set(1, i);
            processor.process(DatabaseFilterManager.findAll(this, criteriaList));
        }
    }
}
