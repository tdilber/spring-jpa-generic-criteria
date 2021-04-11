package com.beyt.filter;

import com.beyt.dto.Criteria;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.filter.rule.specification.*;
import com.beyt.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    public static <Entity> List<Entity> getEntityListBySelectableFilter(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        Class<Entity> entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(repositoryExecutor.getClass(), JpaSpecificationExecutor.class);
        return getEntityListBySelectableFilterWithReturnType(repositoryExecutor, searchQuery, entityClass);
    }

    @SuppressWarnings("unchecked")
    public static <Entity> List<Tuple> getEntityListBySelectableFilterWithTuple(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        return getEntityListWithReturnClass(repositoryExecutor, searchQuery, Tuple.class);
    }

    @SuppressWarnings("unchecked")
    public static <Entity, ResultType> List<ResultType> getEntityListBySelectableFilterWithReturnType(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        Class<Entity> entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(repositoryExecutor.getClass(), JpaSpecificationExecutor.class);
        if (resultTypeClass.equals(entityClass) && CollectionUtils.isEmpty(searchQuery.getSelect())) {
            return getEntityListWithReturnClass(repositoryExecutor, searchQuery, resultTypeClass);
        } else {
            List<Tuple> entityListBySelectableFilter = getEntityListWithReturnClass(repositoryExecutor, searchQuery, Tuple.class);

            if (!CollectionUtils.isEmpty(searchQuery.getSelect())) {
                return convertResultToResultTypeList(searchQuery.getSelect(), resultTypeClass, entityListBySelectableFilter);
            } else {

                if (!CollectionUtils.isEmpty(entityListBySelectableFilter)) {
                    List<Pair<String, String>> parameters = entityListBySelectableFilter.get(0).getElements().stream().filter(e -> SingularAttributePath.class.isAssignableFrom(e.getClass()))
                            .map(e -> Pair.of(((SingularAttributePath) e).getAttribute().getName(), Objects.isNull(e.getAlias()) ? ((SingularAttributePath) e).getAttribute().getName() : e.getAlias())).collect(Collectors.toList());
                    return convertResultToResultTypeList(parameters, resultTypeClass, entityListBySelectableFilter);
                } else {
                    return new ArrayList<>();
                }
            }
        }
    }

    private static <Entity, ResultType> List<ResultType> getEntityListWithReturnClass(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        Class<Entity> entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(repositoryExecutor.getClass(), JpaSpecificationExecutor.class);
        EntityManager entityManager = ApplicationContextUtil.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResultType> query = builder.createQuery(resultTypeClass);
        Root<Entity> root = query.from(entityClass);

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
            query.where(new GenericSpecification<Entity>(searchQuery.getWhere()).toPredicate(root, query, builder));
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
            }
        }

        return typedQuery.getResultList();
    }

    private static <ResultType> List<ResultType> convertResultToResultTypeList(List<Pair<String, String>> querySelects, Class<ResultType> resultTypeClass, List<Tuple> entityListBySelectableFilter) {
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

        return entityListBySelectableFilter.stream().map(t -> {
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
    }

    private static <M extends Map<CriteriaType, ?>> List<Criteria> getMapSpecificRules(M map, List<Criteria> searchCriteriaList) {
        List<Criteria> result = new ArrayList<>();

        for (Criteria criteria : searchCriteriaList) {
            if (map.containsKey(criteria.operation)) {
                result.add(criteria);
            }
        }

        return result;
    }
}
