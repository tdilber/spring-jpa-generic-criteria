package com.beyt.jdq.query.rule.specification;


import com.beyt.jdq.dto.Criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

/**
 * Created by tdilber at 25-Aug-19
 */
public interface ISpecificationFilterRule {
    Predicate generatePredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria);
}
