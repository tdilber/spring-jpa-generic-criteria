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
public class SpecificationFilterSpecifiedRule implements ISpecificationFilterRule {

    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        SpecificationUtil.checkHasFirstValue(criteria);
        if (!criteria.values.get(0).toString().equalsIgnoreCase("true") && !criteria.values.get(0).toString().equalsIgnoreCase("false")) {
            throw new DynamicQueryNoAvailableValueException("Specified rule first value must be true or false. But you send " + criteria.values.get(0).toString());
        }

        return criteria.values.get(0).toString().equalsIgnoreCase("true") ? builder.isNotNull(root.get(criteria.key)) : builder.isNull(root.get(criteria.key));
    }
}
