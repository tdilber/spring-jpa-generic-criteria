package com.beyt.filter.rule.specification;

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
public class SpecificationFilterNotEqualRule implements ISpecificationFilterRule {

    @Override
    public Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        SpecificationUtil.checkHasFirstValue(criteria);
        Predicate[] predicates = new Predicate[criteria.values.size()];
        for (int i = 0; i < criteria.values.size(); i++) {
            predicates[i] = builder.notEqual(root.get(criteria.key), criteria.values.get(i));
        }

        return builder.and(predicates);
    }
}
