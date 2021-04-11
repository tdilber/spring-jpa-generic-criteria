package com.beyt.filter;

import com.beyt.dto.enums.JoinType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericSpecificationTest {

    @Test
    void getFieldName() {
        Assertions.assertEquals("name", GenericSpecification.getFieldName("asd.asd<asd>name"));
    }

    @Test
    void getFieldJoins() {
        List<Pair<String, JoinType>> fieldJoins = new ArrayList<>();
        fieldJoins.add(Pair.of("asd2fg", JoinType.INNER));
        fieldJoins.add(Pair.of("asdhj", JoinType.LEFT));
        fieldJoins.add(Pair.of("asdasda", JoinType.RIGHT));

        assertEquals(fieldJoins, GenericSpecification.getFieldJoins("asd2fg.asdhj<asdasda>name"));
    }
}
