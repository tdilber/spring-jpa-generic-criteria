package com.beyt.filter.rule.specification;

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
public class SpecificationFilterLeftLikeRule implements ISpecificationFilterRule {

    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        SpecificationUtil.checkHasFirstValue(criteria);
        Predicate[] predicates = new Predicate[criteria.values.size()];
        for (int i = 0; i < criteria.values.size(); i++) {
            if (root.get(criteria.key).getJavaType() == String.class) {
                predicates[i] = builder.like(root.<String>get(criteria.key), "%" + criteria.values.get(i));
            } else {
                throw new DynamicQueryNoAvailableValueException("Need String Type: " + criteria.key);
            }
        }

        return builder.or(predicates);
    }
}
