package com.beyt.query;

import com.beyt.dto.Criteria;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.dto.enums.JoinType;
import com.beyt.exception.*;
import com.beyt.util.ApplicationContextUtil;
import com.beyt.util.SpecificationUtil;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tdilber at 24-Aug-19
 */

public class DynamicSpecification<Entity> implements Specification<Entity> {

    protected List<Criteria> criteriaList;
    protected Map<Triple<String, String, JoinType>, Join<?, ?>> joinMap = new ConcurrentHashMap<>();

    public DynamicSpecification(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
        this.joinMap = new ConcurrentHashMap<>();
    }

    public DynamicSpecification(List<Criteria> criteriaList, Map<Triple<String, String, JoinType>, Join<?, ?>> joinMap) {
        this.criteriaList = criteriaList;
        this.joinMap = joinMap;
    }

    @Override
    public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicateAndList = new ArrayList<>();
        List<Predicate> predicateOrList = new ArrayList<>();
        for (int i = 0; i < criteriaList.size(); i++) {
            if (criteriaList.get(i).operation == CriteriaType.PARENTHES) {
                SpecificationUtil.checkHasFirstValue(criteriaList.get(i));
                try {
                    predicateAndList.add(new DynamicSpecification<Entity>(((List<Criteria>) (criteriaList.get(i).values.get(0))), joinMap).toPredicate(root, query, builder));
                } catch (Exception e) {
                    throw new DynamicQueryNoAvailableParenthesesOperationUsageException(
                            "There is No Available Paranthes Operation Usage in Criteria Key: " + criteriaList.get(i).key);
                }
            } else if (criteriaList.get(i).operation == CriteriaType.OR) {
                if (i == 0 || i + 1 == criteriaList.size()) {
                    throw new DynamicQueryNoAvailableOrOperationUsageException(
                            "There is No Available OR Operation Usage in Criteria Key: " + criteriaList.get(i).key);
                }

                predicateOrList.add(builder.and(predicateAndList.toArray(new Predicate[0])));
                predicateAndList.clear();
            } else {
                predicateAndList.add(getPredicate(root, builder, criteriaList.get(i)));
            }
        }

        predicateOrList.add(builder.and(predicateAndList.toArray(new Predicate[0])));

        return builder.or(predicateOrList.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(Root<Entity> root, CriteriaBuilder builder, Criteria criteria) {
        From<?, ?> localFrom = createLocalFrom(root, criteria.key);
        return addPredicate(localFrom, builder, new Criteria(getFieldName(criteria.key), criteria.operation, criteria.values.toArray(new Object[0])));
    }

    public From<?, ?> createLocalFrom(Root<?> root, String key) {
        From<?, ?> localFrom = root;
        List<Pair<String, JoinType>> fieldJoins = getFieldJoins(key);

        for (Pair<String, JoinType> fieldJoin : fieldJoins) {
            localFrom = getJoin(localFrom, fieldJoin.getFirst(), fieldJoin.getSecond());
        }

        return localFrom;
    }

    public static List<Pair<String, JoinType>> getFieldJoins(String key) {
        List<Pair<String, JoinType>> fieldJoins = new ArrayList<>();

        String subKey = key;
        JoinType joinType = null;
        int index = -1;


        while (subKey.chars().anyMatch(c -> Arrays.stream(JoinType.values()).anyMatch(j -> j.getSeparator().charValue() == c))) {

            for (JoinType value : JoinType.values()) {
                int indexOf = subKey.indexOf(value.getSeparator());
                if (indexOf > -1 && (index == -1 || indexOf < index)) {
                    index = indexOf;
                    joinType = value;
                }
            }

            if (Objects.nonNull(joinType)) {
                fieldJoins.add(Pair.of(subKey.substring(0, index), joinType));
                subKey = subKey.substring(index + 1);
            }

            index = -1;
            joinType = null;
        }

        return fieldJoins;
    }

    public static String getFieldName(String key) {
        String[] splitedKey = key.split(">|<|\\.");
        return splitedKey[splitedKey.length - 1];
    }

    protected Predicate addPredicate(Path<?> root, CriteriaBuilder builder, Criteria criteria) {
        if (!criteria.operation.equals(CriteriaType.SPECIFIED)) {
            try {
                criteria.values = deserialize(root.get(criteria.key).getJavaType(), criteria.values);
            } catch (Exception e) {
                throw new DynamicQueryNoAvailableEnumException("There is a "
                        + root.get(criteria.key).getJavaType().getSimpleName() + " Enum Problem in Criteria Key: "
                        + criteria.key, e);
            }
        }

        if (DynamicQueryManager.specificationRuleMap.containsKey(criteria.operation)) {
            return DynamicQueryManager.specificationRuleMap
                    .get(criteria.operation).generatePredicate(root, builder, criteria);
        } else {
            throw new DynamicQueryNoAvailableOperationException("There is No Available Operation in Criteria Key: "
                    + criteria.key);
        }
    }

    protected Join<?, ?> getJoin(From<?, ?> from, String key, JoinType joinType) {
        Triple<String, String, JoinType> joinMapKey = new ImmutableTriple<>(((RootImpl) from).getEntityType().getJavaType().getName(), key, joinType);
        if (joinMap.containsKey(joinMapKey)) {
            return joinMap.get(joinMapKey);
        }
        Join<?, ?> join = from.join(key, joinType.getJoinType());
        joinMap.put(joinMapKey, join);
        return join;
    }

    protected List<Object> deserialize(Class<?> clazz, List<Object> objects) throws Exception {
        List<Object> result = new ArrayList<>();
        for (Object object : objects) {
            Object deserialized = null;
            try {
                deserialized = ApplicationContextUtil.getDeserializer().deserialize(object.toString(), clazz);
            } catch (Exception e) {
                throw new DynamicQueryValueSerializeException("There is a "
                        + clazz.getSimpleName() + " Deserialization Problem in Criteria Value: "
                        + object.toString());
            }
            result.add(deserialized);
        }
        return result;
    }
}
