package com.beyt.filter.rule.sort;

import org.springframework.data.domain.Sort;

/**
 * Created by tdilber at 28-Aug-19
 */
public interface ISortFilterRule {
    Sort getSortFilterRule(String field);
}
