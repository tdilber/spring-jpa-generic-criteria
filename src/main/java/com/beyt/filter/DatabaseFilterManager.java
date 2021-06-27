package com.beyt.filter;

import com.beyt.dto.Criteria;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.filter.rule.specification.*;
import com.beyt.repository.GenericSpecificationRepositoryImpl;
import com.beyt.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tdilber at 28-Aug-19
 */
@Slf4j
public class DatabaseFilterManager {

    public final static Map<CriteriaType, ISpecificationFilterRule> specificationRuleMap = new HashMap<>();

    static {
        specificationRuleMap.put(CriteriaType.CONTAIN, new SpecificationFilterDoubleLikeRule());
        specificationRuleMap.put(CriteriaType.DOES_NOT_CONTAIN, new SpecificationFilterDoesNotContainRule());
        specificationRuleMap.put(CriteriaType.END_WITH, new SpecificationFilterLeftLikeRule());
        specificationRuleMap.put(CriteriaType.START_WITH, new SpecificationFilterRightLikeRule());
        specificationRuleMap.put(CriteriaType.SPECIFIED, new SpecificationFilterSpecifiedRule());
        specificationRuleMap.put(CriteriaType.EQUAL, new SpecificationFilterEqualRule());
        specificationRuleMap.put(CriteriaType.NOT_EQUAL, new SpecificationFilterNotEqualRule());
        specificationRuleMap.put(CriteriaType.GREATER_THAN, new SpecificationFilterGreaterThanRule());
        specificationRuleMap.put(CriteriaType.GREATER_THAN_OR_EQUAL, new SpecificationFilterGreaterThanOrEqualToRule());
        specificationRuleMap.put(CriteriaType.LESS_THAN, new SpecificationFilterLessThanRule());
        specificationRuleMap.put(CriteriaType.LESS_THAN_OR_EQUAL, new SpecificationFilterLessThanOrEqualToRule());
        specificationRuleMap.put(CriteriaType.OR, null);
        specificationRuleMap.put(CriteriaType.PARENTHES, null);
    }

    public static <Entity> List<Entity> findAll(JpaSpecificationExecutor<Entity> repositoryExecutor,
                                                List<Criteria> searchCriteriaList) {
        List<Entity> result;
        List<Criteria> specificationRules = getMapSpecificRules(specificationRuleMap, searchCriteriaList);
        GenericSpecification<Entity> specification = null;

        if (!specificationRules.isEmpty()) {
            specification = new GenericSpecification<>(specificationRules);
        }

        return repositoryExecutor.findAll(specification);
    }

    public static <Entity> Page<Entity> findAll(JpaSpecificationExecutor<Entity> repositoryExecutor,
                                                List<Criteria> searchCriteriaList, Pageable pageable) {
        List<Criteria> specificationRules = getMapSpecificRules(specificationRuleMap, searchCriteriaList);

        GenericSpecification<Entity> specification = null;
        if (!specificationRules.isEmpty()) {
            specification = new GenericSpecification<>(specificationRules);
        }

        return repositoryExecutor.findAll(specification, pageable);
    }

    public static <Entity> Specification<Entity> getSpecification(List<Criteria> searchCriteriaList) {
        List<Criteria> specificationRules = getMapSpecificRules(specificationRuleMap, searchCriteriaList);

        GenericSpecification<Entity> specification = null;
        if (!specificationRules.isEmpty()) {
            specification = new GenericSpecification<>(specificationRules);
        }

        return specification;
    }

    public static <Entity> long count(JpaSpecificationExecutor<Entity> repositoryExecutor,
                                      List<Criteria> searchCriteriaList) {
        Long result = 0l;
        List<Criteria> specificationRules = getMapSpecificRules(specificationRuleMap, searchCriteriaList);
        GenericSpecification<Entity> specification = null;
        if (specificationRules == null || !specificationRules.isEmpty()) {
            specification = new GenericSpecification<>(specificationRules);
        }
        result = repositoryExecutor.count(specification);

        return result;
    }

    public static <Entity> List<Entity> getEntityListBySelectableFilterAsList(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        return (List<Entity>) DatabaseFilterManager.getEntityListBySelectableFilter(repositoryExecutor, searchQuery, false);
    }

    public static <Entity> Page<Entity> getEntityListBySelectableFilterAsPage(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        return (Page<Entity>) DatabaseFilterManager.getEntityListBySelectableFilter(repositoryExecutor, searchQuery, true);
    }

    protected static <Entity> Iterable<Entity> getEntityListBySelectableFilter(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, boolean isPage) {
        Class<Entity> entityClass = getEntityClass(repositoryExecutor);
        return getEntityListBySelectableFilterWithReturnType(repositoryExecutor, searchQuery, entityClass, isPage);
    }

    public static <Entity> List<Tuple> getEntityListBySelectableFilterWithTupleAsList(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        return (List<Tuple>) DatabaseFilterManager.getEntityListBySelectableFilterWithTuple(repositoryExecutor, searchQuery, false);
    }

    public static <Entity> Page<Tuple> getEntityListBySelectableFilterWithTupleAsPage(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        return (Page<Tuple>) DatabaseFilterManager.getEntityListBySelectableFilterWithTuple(repositoryExecutor, searchQuery, true);
    }

    protected static <Entity> Iterable<Tuple> getEntityListBySelectableFilterWithTuple(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, boolean isPage) {
        return getEntityListWithReturnClass(repositoryExecutor, searchQuery, Tuple.class, isPage);
    }

    public static <Entity, ResultType> List<ResultType> getEntityListBySelectableFilterWithReturnTypeAsList(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        return (List<ResultType>) DatabaseFilterManager.getEntityListBySelectableFilterWithReturnType(repositoryExecutor, searchQuery, resultTypeClass, false);
    }

    public static <Entity, ResultType> Page<ResultType> getEntityListBySelectableFilterWithReturnTypeAsPage(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        return (Page<ResultType>) DatabaseFilterManager.getEntityListBySelectableFilterWithReturnType(repositoryExecutor, searchQuery, resultTypeClass, true);
    }

    protected static <Entity, ResultType> Iterable<ResultType> getEntityListBySelectableFilterWithReturnType(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass, boolean isPage) {
        Class<Entity> entityClass = getEntityClass(repositoryExecutor);
        if (resultTypeClass.equals(entityClass) && CollectionUtils.isEmpty(searchQuery.getSelect())) {
            return (List<ResultType>) getEntityListWithReturnClass(repositoryExecutor, searchQuery, resultTypeClass, isPage);
        } else {
            Iterable<Tuple> entityListBySelectableFilter = getEntityListWithReturnClass(repositoryExecutor, searchQuery, Tuple.class, isPage);

            if (!CollectionUtils.isEmpty(searchQuery.getSelect())) {
                return convertResultToResultTypeList(searchQuery.getSelect(), resultTypeClass, entityListBySelectableFilter, isPage);
            } else {

                if (!IterableUtils.isEmpty(entityListBySelectableFilter)) {
                    List<Pair<String, String>> parameters = entityListBySelectableFilter.iterator().next().getElements().stream().filter(e -> SingularAttributePath.class.isAssignableFrom(e.getClass()))
                            .map(e -> Pair.of(((SingularAttributePath) e).getAttribute().getName(), Objects.isNull(e.getAlias()) ? ((SingularAttributePath) e).getAttribute().getName() : e.getAlias())).collect(Collectors.toList());
                    return convertResultToResultTypeList(parameters, resultTypeClass, entityListBySelectableFilter, isPage);
                } else {
                    return new ArrayList<>();
                }
            }
        }
    }

    protected static <Entity, ResultType> Iterable<ResultType> getEntityListWithReturnClass(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass, boolean isPage) {
        Class<Entity> entityClass = getEntityClass(repositoryExecutor);
        EntityManager entityManager = ApplicationContextUtil.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResultType> query = builder.createQuery(resultTypeClass);
        Root<Entity> root = query.from(entityClass);
        Specification<Entity> specification = null;
        Pageable pageable = Pageable.unpaged();

        query.distinct(searchQuery.isDistinct());

        if (!CollectionUtils.isEmpty(searchQuery.getSelect())) {
            List<Selection<?>> selectionList = new ArrayList<>();

            searchQuery.getSelect().forEach(selectField -> {
                selectionList.add(GenericSpecification.createLocalFrom(root, selectField.getFirst()).get(GenericSpecification.getFieldName(selectField.getFirst())).alias(selectField.getSecond()));
            });
            query.multiselect(selectionList);
        } else if (!resultTypeClass.equals(entityClass)) {
            List<Selection<?>> selectionList = new ArrayList<>();
            Set<SingularAttributeImpl> declaredAttributes = ((RootImpl) root).getModel().getDeclaredAttributes();
            for (SingularAttributeImpl declaredAttribute : declaredAttributes) {
                selectionList.add(root.get(declaredAttribute.getName()));
            }
            query.multiselect(selectionList);
        }

        if (!CollectionUtils.isEmpty(searchQuery.getWhere())) {
            specification = new GenericSpecification<>(searchQuery.getWhere());
            query.where(specification.toPredicate(root, query, builder));
        }

        if (!CollectionUtils.isEmpty(searchQuery.getOrderBy())) {
            List<Order> orderList = new ArrayList<>();
            searchQuery.getOrderBy().forEach((orderPair) -> {
                orderList.add(orderPair.getSecond() == com.beyt.dto.enums.Order.DESC ? builder.desc(GenericSpecification.createLocalFrom(root, orderPair.getFirst()).get(GenericSpecification.getFieldName(orderPair.getFirst()))) : builder.asc(GenericSpecification.createLocalFrom(root, orderPair.getFirst()).get(GenericSpecification.getFieldName(orderPair.getFirst()))));
            });
            query.orderBy(orderList);
        }

        TypedQuery<ResultType> typedQuery = entityManager.createQuery(query);


        if (Objects.nonNull(searchQuery.getPageSize())) {
            typedQuery.setMaxResults(searchQuery.getPageSize());

            if (Objects.nonNull(searchQuery.getPageNumber())) {
                typedQuery.setFirstResult(searchQuery.getPageSize() * searchQuery.getPageNumber());

                if (!CollectionUtils.isEmpty(searchQuery.getOrderBy())) {
                    pageable = PageRequest.of(searchQuery.getPageNumber(), searchQuery.getPageSize(), searchQuery.getOrderBy().get(0).getSecond().getDirection(), searchQuery.getOrderBy().stream().map(Pair::getFirst).toArray(String[]::new));
                } else {
                    pageable = PageRequest.of(searchQuery.getPageNumber(), searchQuery.getPageSize());
                }
            }
        }

        if (isPage) {
            Specification<Entity> finalSpecification = specification;
            return PageableExecutionUtils.getPage(typedQuery.getResultList(), pageable,
                    () -> executeCountQuery(getCountQuery(finalSpecification, entityClass)));
        } else {
            return typedQuery.getResultList();
        }
    }


    protected static <S> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
        EntityManager entityManager = ApplicationContextUtil.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<S> root = applySpecificationToCriteria(spec, domainClass, query);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        // Remove all Orders the Specifications might have applied
        query.orderBy(Collections.<Order>emptyList());

        return entityManager.createQuery(query);
    }

    protected static <S, U> Root<U> applySpecificationToCriteria(@Nullable Specification<U> spec, Class<U> domainClass,
                                                                 CriteriaQuery<S> query) {

        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");
        EntityManager entityManager = ApplicationContextUtil.getEntityManager();
        Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }


    protected static long executeCountQuery(TypedQuery<Long> query) {

        Assert.notNull(query, "TypedQuery must not be null!");

        List<Long> totals = query.getResultList();
        long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }

        return total;
    }

    protected static <ResultType> Iterable<ResultType> convertResultToResultTypeList(List<Pair<String, String>> querySelects, Class<ResultType> resultTypeClass, Iterable<Tuple> entityListBySelectableFilter, boolean isPage) {
        Map<Integer, Method> setterMethods = new HashMap<>();
        for (int i = 0; i < querySelects.size(); i++) {
            String select = querySelects.get(i).getSecond();

            Optional<Method> methodOptional = Arrays.stream(resultTypeClass.getMethods())
                    .filter(c -> c.getName().equalsIgnoreCase("set" + select)
                            && c.getParameterCount() == 1).findFirst();

            if (methodOptional.isPresent()) {
                setterMethods.put(i, methodOptional.get());
            }
        }
        Stream<Tuple> stream = isPage ? ((Page<Tuple>) entityListBySelectableFilter).stream() : ((List<Tuple>) entityListBySelectableFilter).stream();

        List<ResultType> resultTypeList = stream.map(t -> {
            try {
                ResultType resultObj = resultTypeClass.newInstance();

                for (Map.Entry<Integer, Method> entry : setterMethods.entrySet()) {
                    entry.getValue().invoke(resultObj, t.get(entry.getKey()));
                }
                return resultObj;
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (isPage) {
            Page<Tuple> tuplePage = (Page<Tuple>) entityListBySelectableFilter;
            return new PageImpl<>(resultTypeList, tuplePage.getPageable(), tuplePage.getTotalElements());
        } else {
            return resultTypeList;
        }
    }

    @SuppressWarnings("unchecked")
    protected static <Entity> Class<Entity> getEntityClass(JpaSpecificationExecutor<Entity> repositoryExecutor) {
        Class<Entity> entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(repositoryExecutor.getClass(), JpaSpecificationExecutor.class);
        if (Objects.nonNull(entityClass)) {
            return entityClass;
        } else if (repositoryExecutor instanceof GenericSpecificationRepositoryImpl) {
            return ((GenericSpecificationRepositoryImpl) repositoryExecutor).getDomainClass();
        } else {
            throw new IllegalStateException("Entity Class Type Detection Failed!");
        }
    }

    protected static <M extends Map<CriteriaType, ?>> List<Criteria> getMapSpecificRules(M map, List<Criteria> searchCriteriaList) {
        List<Criteria> result = new ArrayList<>();

        for (Criteria criteria : searchCriteriaList) {
            if (map.containsKey(criteria.operation)) {
                result.add(criteria);
            }
        }

        return result;
    }
}
