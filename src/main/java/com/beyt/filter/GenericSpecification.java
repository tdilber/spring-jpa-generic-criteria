package com.beyt.filter;

import com.beyt.dto.Criteria;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.exception.GenericFilterNoAvailableEnumException;
import com.beyt.exception.GenericFilterNoAvailableOperationException;
import com.beyt.exception.GenericFilterNoAvailableOrOperationUsageException;
import com.beyt.exception.GenericFilterNoAvailableParanthesOperationUsageException;
import com.beyt.util.ReflectionUtil;
import com.beyt.util.SpecificationUtil;
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
        List<Predicate> predicateAndList = new ArrayList<>();
        List<Predicate> predicateOrList = new ArrayList<>();
        for (int i = 0; i < criteriaList.size(); i++) {
            if (criteriaList.get(i).operation == CriteriaType.PARENTHES) {
                SpecificationUtil.checkHasFirstValue(criteriaList.get(i));
                try {
                    predicateAndList.add(new GenericSpecification<Entity>(((List<Criteria>) (criteriaList.get(i).values.get(0)))).toPredicate(root, query, builder));
                } catch (Exception e) {
                    throw new GenericFilterNoAvailableParanthesOperationUsageException(
                            "There is No Available Paranthes Operation Usage in Criteria Key: " + criteriaList.get(i).key);
                }
            } else if (criteriaList.get(i).operation == CriteriaType.OR) {
                if (i == 0 || i + 1 == criteriaList.size()) {
                    throw new GenericFilterNoAvailableOrOperationUsageException(
                            "There is No Available OR Operation Usage in Criteria Key: " + criteriaList.get(i).key);
                }

                predicateOrList.add(builder.and(predicateAndList.toArray(new Predicate[0])));
                predicateAndList.clear();
            } else {
                predicateAndList.add(getPredicate(root, builder, criteriaList.get(i)));
            }
        }

        predicateOrList.add(builder.and(predicateAndList.toArray(new Predicate[0])));

        return builder.or(predicateOrList.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(Root<Entity> root, CriteriaBuilder builder, Criteria criteria) {
        From<?, ?> localFrom = createLocalFrom(root, criteria.key);
        return addPredicate(localFrom, builder, new Criteria(getFieldName(criteria.key), criteria.operation, criteria.values.toArray(new Object[0])));
    }

    public static From<?, ?> createLocalFrom(Root<?> root, String key) {
        From<?, ?> localFrom = root;
        String[] splitedKey = key.split("\\.");
        for (int j = 0; j < splitedKey.length - 1; j++) {
            localFrom = getJoin(localFrom, splitedKey[j]);
        }
        return localFrom;
    }

    public static String getFieldName(String key) {
        String[] splitedKey = key.split("\\.");
        return splitedKey[splitedKey.length - 1];
    }

    private Predicate addPredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
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
            return DatabaseFilterManager.specificationRuleMap
                    .get(criteria.operation).generatePredicate(root, builder, criteria);
        } else {
            throw new GenericFilterNoAvailableOperationException("There is No Available Operation in Criteria Key: "
                    + criteria.key);
        }
    }

    private static Join<?, ?> getJoin(From<?, ?> from, String key) {
        return from.join(key);
    }
}
