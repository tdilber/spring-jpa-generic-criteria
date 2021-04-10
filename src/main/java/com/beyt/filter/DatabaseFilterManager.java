package com.beyt.filter;

import com.beyt.dto.Criteria;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.filter.rule.sort.ISortFilterRule;
import com.beyt.filter.rule.sort.SortFilterAscRule;
import com.beyt.filter.rule.sort.SortFilterDescRule;
import com.beyt.filter.rule.specification.*;
import com.beyt.filter.rule.top.*;
import com.beyt.repository.JpaExtendedRepository;
import com.beyt.util.ApplicationContextUtil;
import com.beyt.util.SpecificationUtil;
import com.beyt.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tdilber at 28-Aug-19
 */
@Slf4j
public class DatabaseFilterManager {

    public final static Map<CriteriaType, ISpecificationFilterRule> specificationRuleMap = new HashMap<>();
    public final static Map<CriteriaType, IPageFilterRule> topRuleMap = new HashMap<>();
    public final static Map<CriteriaType, ISortFilterRule> sortRuleMap = new HashMap<>();
    public final static List<CriteriaType> allRuleOperationList = new ArrayList<>();

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

        topRuleMap.put(CriteriaType.TOP_ASC, new TopFilterAscRule());
        topRuleMap.put(CriteriaType.TOP_DESC, new TopFilterDescRule());
        topRuleMap.put(CriteriaType.PAGE_ASC, new PageFilterAscRule());
        topRuleMap.put(CriteriaType.PAGE_DESC, new PageFilterDescRule());

        sortRuleMap.put(CriteriaType.SORT_ASC, new SortFilterAscRule());
        sortRuleMap.put(CriteriaType.SORT_DESC, new SortFilterDescRule());

        allRuleOperationList.addAll(specificationRuleMap.keySet());
        allRuleOperationList.addAll(topRuleMap.keySet());
        allRuleOperationList.addAll(sortRuleMap.keySet());
    }

    public static <Entity> List<Entity> findAll(JpaSpecificationExecutor<Entity> repositoryExecutor,
                                                List<Criteria> searchCriteriaList) {
        List<Entity> result;

        List<Criteria> topRules = getMapSpecificRules(topRuleMap, searchCriteriaList);
        List<Criteria> sortRules = getMapSpecificRules(sortRuleMap, searchCriteriaList);
        List<Criteria> specificationRules = getMapSpecificRules(specificationRuleMap, searchCriteriaList);
        GenericSpecification<Entity> specification = null;

        if (!specificationRules.isEmpty()) {
            specification = new GenericSpecification<>(specificationRules);
        }

        if (!topRules.isEmpty()) {
            Criteria criteria = topRules.get(0);
            IPageFilterRule topFilterRule = topRuleMap.get(criteria.operation);
            Pageable pageable;
            if (criteria.operation == CriteriaType.TOP_ASC || criteria.operation == CriteriaType.TOP_DESC) {
                SpecificationUtil.checkHasFirstValue(criteria);
                pageable = topFilterRule.generatePageRequest(0, Integer.parseInt(criteria.values.get(0).toString()), criteria.key);
            } else {
                SpecificationUtil.checkHasTwoValue(criteria);
                pageable = topFilterRule.generatePageRequest(Integer.parseInt(criteria.values.get(1).toString()), Integer.parseInt(criteria.values.get(0).toString()), criteria.key);
            }
            Page<Entity> entityPage = repositoryExecutor.findAll(specification, pageable);
            result = entityPage.getContent();
        } else if (!sortRules.isEmpty()) {
            Criteria criteria = sortRules.get(0);
            ISortFilterRule sortFilterRule = sortRuleMap.get(criteria.operation);
            result = repositoryExecutor.findAll(specification, sortFilterRule.getSortFilterRule(criteria.key));
        } else {
            result = repositoryExecutor.findAll(specification);
        }

        return result;
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
    public static <Entity> List<Tuple> getEntityListBySelectableFilter(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery) {
        Class<Entity> entityClass = (Class<Entity>)GenericTypeResolver.resolveTypeArgument(repositoryExecutor.getClass(), JpaSpecificationExecutor.class);
        EntityManager entityManager = ApplicationContextUtil.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Entity> root = query.from(entityClass);

        query.distinct(searchQuery.isDistinct());

        if (!CollectionUtils.isEmpty(searchQuery.getSelect())) {
            List<Selection<?>> selectionList = new ArrayList<>();
            searchQuery.getSelect().forEach(selectField -> selectionList.add(root.get(selectField)));
            query.multiselect(selectionList);
        }

        if (!CollectionUtils.isEmpty(searchQuery.getWhere())) {
            query.where(new GenericSpecification<Entity>(searchQuery.getWhere()).toPredicate(root, query, builder));
        }

        if (!CollectionUtils.isEmpty(searchQuery.getOrderBy())) {
            List<Order> orderList = new ArrayList<>();
            searchQuery.getOrderBy().forEach((field, order) -> orderList.add(order == com.beyt.dto.enums.Order.DESC ? builder.desc(root.get(field)) : builder.asc(root.get(field))));
            query.orderBy(orderList);
        }

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);


        if (Objects.nonNull(searchQuery.getPageSize())) {
            typedQuery.setMaxResults(searchQuery.getPageSize());

            if (Objects.nonNull(searchQuery.getPageNumber())) {
                typedQuery.setFirstResult(searchQuery.getPageSize() * searchQuery.getPageNumber());
            }
        }

        return typedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public static <Entity, ResultType> List<ResultType> getEntityListBySelectableFilter(JpaSpecificationExecutor<Entity> repositoryExecutor, SearchQuery searchQuery, Class<ResultType> resultTypeClass) {
        List<Tuple> entityListBySelectableFilter = getEntityListBySelectableFilter(repositoryExecutor, searchQuery);

        if (!CollectionUtils.isEmpty(searchQuery.getWhere())) {
            Map<Integer, Method> setterMethods = new HashMap<>();
            for (int i = 0; i < searchQuery.getSelect().size(); i++) {
                String select = searchQuery.getSelect().get(i);

                Optional<Method> methodOptional = Arrays.stream(resultTypeClass.getMethods())
                        .filter(c -> c.getName().equals("set" + StringUtil.uppercaseFirstLetter(select))
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

        }else {
            // TODO
            return null;
        }

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
