package com.beyt.dto;

import com.beyt.dto.enums.CriteriaType;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by tdilber at 11/18/2020
 */
@Slf4j
public class CriteriaFilter extends ArrayList<Criteria> implements Serializable {

    public static CriteriaFilter of(Criteria... criteria) {
        CriteriaFilter criteriaFilter = new CriteriaFilter();
        criteriaFilter.addAll(Arrays.asList(criteria));
        return criteriaFilter;
    }

    public static CriteriaFilter of(Collection<Criteria> criteria) {
        CriteriaFilter criteriaFilter = new CriteriaFilter();
        criteriaFilter.addAll(criteria);
        return criteriaFilter;
    }

    public List<Criteria> getCriteriaListByKeyAndOperation(String key, CriteriaType operation) {
        ArrayList<Criteria> criterias = new ArrayList<>();

        for (Criteria criteria : this) {
            if (criteria.key.equalsIgnoreCase(key) && criteria.operation.equals(operation)) {
                criterias.add(criteria);
            }
        }

        return criterias;
    }


    public List<Criteria> getCriteriaListByKey(String key) {
        ArrayList<Criteria> criterias = new ArrayList<>();

        for (Criteria criteria : this) {
            if (criteria.key.equalsIgnoreCase(key)) {
                criterias.add(criteria);
            }
        }

        return criterias;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Criteria criteria : this) {
            result.append("{").append(criteria.toString()).append("}");
        }

        return result.toString();
    }
}
