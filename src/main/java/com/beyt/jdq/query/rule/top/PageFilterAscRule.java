package com.beyt.jdq.query.rule.top;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by tdilber at 28-Aug-19
 */
public class PageFilterAscRule implements IPageFilterRule {

    @Override
    public Pageable generatePageRequest(int page, int size, String field) {
        return PageRequest.of(page, size, Sort.Direction.ASC, field);
    }
}
