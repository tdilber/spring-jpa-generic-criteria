package com.beyt.query.rule.specification;

import com.beyt.dto.Criteria;
import com.beyt.util.SpecificationUtil;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by tdilber at 25-Aug-19
 */
@Slf4j
public class SpecificationFilterGreaterThanOrEqualToRule implements ISpecificationFilterRule {
    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        Predicate predicate;
        SpecificationUtil.checkHasFirstValue(criteria);
        predicate = builder.greaterThanOrEqualTo(root.<Comparable>get(criteria.getKey()), (Comparable) criteria.getValues().get(0));

        return predicate;
    }
}
