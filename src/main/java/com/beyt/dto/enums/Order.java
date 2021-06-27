package com.beyt.dto.enums;

import org.springframework.data.domain.Sort;

/**
 * {@code Order} defines ascending and descending order
 *
 * @author tiwe
 */
public enum Order {
    /**
     * Ascending order
     */
    ASC(Sort.Direction.ASC),
    /**
     * Descending order
     */
    DESC(Sort.Direction.DESC);

    Order(Sort.Direction direction) {
        this.direction = direction;
    }

    private final Sort.Direction direction;

    public Sort.Direction getDirection() {
        return direction;
    }
}
