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
public class SpecificationFilterLessThanRule implements ISpecificationFilterRule {

    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        Predicate predicate;
        SpecificationUtil.checkHasFirstValue(criteria);
        predicate = builder.lessThan(root.<Comparable>get(criteria.getKey()), (Comparable) criteria.getValues().get(0));

        return predicate;
    }
}
