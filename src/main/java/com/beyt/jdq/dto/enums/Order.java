package com.beyt.jdq.dto.enums;

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

    public static Order of(String orderText) {
        if (orderText.equalsIgnoreCase("desc")) {
            return DESC;
        } else if (orderText.equalsIgnoreCase("asc")) {
            return ASC;
        } else {
            throw new IllegalArgumentException("Order Direction Text not valid!");
        }
    }
}
