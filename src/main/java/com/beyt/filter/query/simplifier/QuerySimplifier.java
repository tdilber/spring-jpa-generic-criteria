package com.beyt.filter.query.simplifier;

import com.beyt.dto.Criteria;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.dto.enums.Order;
import com.beyt.filter.GenericSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

public class QuerySimplifier {
    public static final Criteria OR = Criteria.of("", CriteriaType.OR);

    public static Field Field(String name) {
        return new Field(name);
    }

    public static Criteria Parantesis(Criteria... criterias) {
        return Criteria.of("", CriteriaType.PARENTHES, Arrays.asList(criterias));
    }

    public static OrderByRule OrderBy(String fieldName, Order orderType) {
        return new OrderByRule(fieldName, orderType);
    }

    public static SelectRule Select(String fieldName, String alias) {
        return new SelectRule(fieldName, alias);
    }

    public static SelectRule Select(String fieldName) {
        return new SelectRule(fieldName, GenericSpecification.getFieldName(fieldName));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderByRule {
        private String fieldName;
        private Order orderType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectRule {
        private String fieldName;
        private String alias;
    }

    public static class Field {
        private String name;

        public Field(String name) {
            this.name = name;
        }

        public Criteria eq(Object... values) {
            return Criteria.of(name, CriteriaType.EQUAL, values);
        }

        public Criteria notEq(Object... values) {
            return Criteria.of(name, CriteriaType.NOT_EQUAL, values);
        }

        public Criteria contain(Object... values) {
            return Criteria.of(name, CriteriaType.CONTAIN, values);
        }

        public Criteria doesNotContain(Object... values) {
            return Criteria.of(name, CriteriaType.DOES_NOT_CONTAIN, values);
        }

        public Criteria endWith(Object... values) {
            return Criteria.of(name, CriteriaType.END_WITH, values);
        }

        public Criteria startWith(Object... values) {
            return Criteria.of(name, CriteriaType.START_WITH, values);
        }

        public Criteria specified(boolean value) {
            return Criteria.of(name, CriteriaType.SPECIFIED, value);
        }

        public Criteria isNull() {
            return Criteria.of(name, CriteriaType.SPECIFIED, false);
        }

        public Criteria nonNull() {
            return Criteria.of(name, CriteriaType.SPECIFIED, true);
        }

        public Criteria greaterThan(Object value) {
            return Criteria.of(name, CriteriaType.GREATER_THAN, value);
        }

        public Criteria greaterThanOrEqual(Object value) {
            return Criteria.of(name, CriteriaType.GREATER_THAN_OR_EQUAL, value);
        }

        public Criteria lessThan(Object value) {
            return Criteria.of(name, CriteriaType.LESS_THAN, value);
        }

        public Criteria lessThanOrEqual(Object value) {
            return Criteria.of(name, CriteriaType.LESS_THAN_OR_EQUAL, value);
        }
    }
}
