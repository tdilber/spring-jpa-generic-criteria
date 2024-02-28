package com.beyt.query.rule.specification;

import com.beyt.dto.Criteria;
import com.beyt.exception.DynamicQueryNoAvailableValueException;
import com.beyt.util.SpecificationUtil;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by tdilber at 25-Aug-19
 */
@Slf4j
public class SpecificationFilterDoesNotContainRule implements ISpecificationFilterRule {

    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        SpecificationUtil.checkHasFirstValue(criteria);
        Predicate[] predicates = new Predicate[criteria.getValues().size()];
        for (int i = 0; i < criteria.getValues().size(); i++) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                predicates[i] = builder.notLike(root.<String>get(criteria.getKey()), "%" + criteria.getValues().get(i) + "%");
            } else {
                throw new DynamicQueryNoAvailableValueException("Need String Type: " + criteria.getKey());
            }
        }

        return builder.and(predicates);
    }
}
