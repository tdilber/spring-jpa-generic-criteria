package com.beyt.filter;

import com.beyt.dto.Criteria;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.exception.GenericFilterNoAvailableEnumException;
import com.beyt.exception.GenericFilterNoAvailableOperationException;
import com.beyt.exception.GenericFilterNoAvailableOrOperationUsageException;
import com.beyt.util.ReflectionUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tdilber at 24-Aug-19
 */

public class GenericSpecification<Entity> implements Specification<Entity> {

    protected List<Criteria> criteriaList;

    public GenericSpecification(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Criteria> andAllCriteriaList = new ArrayList<>();
        List<Predicate> predicateList = new ArrayList<>();
        for (int i = 0; i < criteriaList.size(); i++) {
            if (criteriaList.get(i).operation == CriteriaType.OR) {
                if (i == 0 || i + 1 == criteriaList.size()) {
                    throw new GenericFilterNoAvailableOrOperationUsageException(
                            "There is No Available OR Operation Usage in Criteria Key: " + criteriaList.get(i).key);
                }

                predicateList.add(andAllPredicates(root, builder, andAllCriteriaList));
                andAllCriteriaList.clear();
            } else {
                andAllCriteriaList.add(criteriaList.get(i));
            }
        }

        predicateList.add(andAllPredicates(root, builder, andAllCriteriaList));

        return builder.or(predicateList.toArray(new Predicate[0]));
    }

    private Predicate andAllPredicates(Root<Entity> root, CriteriaBuilder builder, List<Criteria> criteriaList) {
        Predicate[] predicates = new Predicate[criteriaList.size()];

        for (int i = 0; i < criteriaList.size(); i++) {
            From<?, ?> localFrom = root;
            String[] splitedKey = criteriaList.get(i).key.split("\\.");
            for (int j = 0; j < splitedKey.length - 1; j++) {
                localFrom = getJoin(localFrom, splitedKey[j]);
            }
            addPredicate(localFrom, builder, predicates, new Criteria(splitedKey[splitedKey.length - 1], criteriaList.get(i).operation, criteriaList.get(i).values.toArray(new Object[0])), i);
        }

        Predicate predicate = null;
        if (!criteriaList.isEmpty()) {
            predicate = builder.and(predicates);
        }

        return predicate;
    }

    private void addPredicate(Path<?> root, CriteriaBuilder builder, Predicate[] predicates, Criteria criteria, int i) {
        if (!criteria.operation.equals(CriteriaType.SPECIFIED)) {
            try {
                criteria.values = ReflectionUtil.convertObjectArrayToIfNotAvailable(root.get(criteria.key).getJavaType(), criteria.values);
            } catch (Exception e) {
                throw new GenericFilterNoAvailableEnumException("There is a "
                        + root.get(criteria.key).getJavaType().getSimpleName() + " Enum Problem in Criteria Key: "
                        + criteria.key, e);
            }
        }

        if (DatabaseFilterManager.specificationRuleMap.containsKey(criteria.operation)) {
            predicates[i] = DatabaseFilterManager.specificationRuleMap
                    .get(criteria.operation).generatePredicate(root, builder, criteria);
        } else {
            throw new GenericFilterNoAvailableOperationException("There is No Available Operation in Criteria Key: "
                    + criteria.key);
        }
    }

    private Join<?, ?> getJoin(From<?, ?> from, String key) {
        return from.join(key);
    }
}
