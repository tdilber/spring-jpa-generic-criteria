package com.beyt.dto;


import com.beyt.dto.enums.CriteriaType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by tdilber at 24-Aug-19
 */
@Getter
@Setter
public class Criteria implements Serializable {
    protected String key;
    protected CriteriaType operation;
    protected List<Object> values;

    public static Criteria of(String key, CriteriaType operation, Collection<Object> values) {
        return new Criteria(key, operation, values);
    }

    public static Criteria of(String key, CriteriaType operation, Object... values) {
        return new Criteria(key, operation, values);
    }

    public Criteria(String key, CriteriaType operation, Object... values) {
        this.key = key;
        this.operation = operation;
        this.values = values != null ? Arrays.asList(values) : null;
    }

    public Criteria() {

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("key: " + key + " Criteria Operation: " + operation.name() + " ");
        for (int i = 0; i < values.size(); i++) {
            result.append("value").append(i).append(": ").append(values.get(i));
        }
        return result.toString();
    }
}
